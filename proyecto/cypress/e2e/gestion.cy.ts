describe('Pruebas de la página de reservas', () => {

  beforeEach(() => {
    cy.visit('http://localhost:4200/login');

    cy.get('#loginEmail').type('alberto.hm.99@gmail.com');
    cy.get('#loginPassword').type('alberto12345');
    cy.get('button[type="submit"]').click();
    cy.wait(4000); 
    cy.visit('http://localhost:4200/administrador'); 
  });

 it('debería mostrar la tabla de reservas con encabezados correctos', () => {
    // Verifica que la tabla está visible
    cy.get('mat-table').should('be.visible');

    // Verifica que los encabezados existen
    cy.get('mat-header-cell').then((headers) => {
      const texts = [...headers].map(h => h.textContent?.trim());
      expect(texts).to.include.members([
        'Nombre',
        'Nº Personas',
        'Estado',
        'Fechas',
        'Acciones'
      ]);
    });
  });

  it('debería tener al menos una fila de datos', () => {
    cy.get('mat-row').should('have.length.greaterThan', 0);
  });

  it('debería mostrar los botones adecuados según el estado de la reserva', () => {
    // Iteramos sobre las filas
    cy.get('mat-row').each(($row) => {
      cy.wrap($row).within(() => {
        cy.get('mat-cell').eq(2).invoke('text').then((estado) => {
          const estadoTrim = estado.trim();
          if (estadoTrim === 'PAGADA' || estadoTrim === 'FINALIZADA') {
            cy.get('button mat-icon').should('contain.text', 'check_circle');
          } else if (estadoTrim === 'CANCELADA') {
            cy.get('button mat-icon').should('contain.text', 'error');
          } else {
            cy.get('button mat-icon').then((icons) => {
              const textos = [...icons].map(i => i.textContent?.trim());
              expect(textos).to.include.members(['check', 'close']);
            });
          }
        });
      });
    });
  });

it('debería aprobar una reserva CONFIRMADA, rellenar el motivo y confirmar', () => {
// 1. Clic sobre el botón "check" de una reserva "CONFIRMADA"
cy.get('mat-row')
  .contains('CONFIRMADA')
  .parents('mat-row')
  .within(() => {
    cy.get('button')
      .find('mat-icon')
      .contains('check')
      .click({ force: true });
  });

// 2. Esperar a que se abra el diálogo y rellenar el motivo
cy.get('mat-dialog-container') // también puede ser mat-mdc-dialog-container
  .should('exist')
  .within(() => {
    // Escribe en el textarea
    cy.get('textarea[matinput]')
      .should('be.visible')
      .type('Motivo de prueba desde Cypress', { force: true });

    // 3. Hacer clic en Aceptar
    cy.contains('button', 'Aceptar')
      .should('be.visible')
      .click({ force: true });
  });
  
// 4. Esperar a que se muestre el mensaje de éxito
cy.get('.mat-mdc-simple-snack-bar > .mat-mdc-snack-bar-label').contains('Reserva confirmada con éxito!', { timeout: 20000 })
 

});




});

