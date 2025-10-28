describe('RegisterComponent', () => {
  const url = 'http://localhost:4200/register'; // ajusta si es diferente

  it('debería registrar un usuario con datos válidos', () => {
    cy.visit(url);

    cy.get('#loginNombre').type('Juan');
    cy.get('#loginApellidos').type('Pérez');
    cy.get('#loginEmail').type('pepito3@yopmail.com');
    cy.get('#loginTelefono').type('666777888');
    cy.get('#loginNif').type('12345678Z');
    cy.get('#loginDireccion').type('Calle Falsa 123');
    cy.get('#loginFecha')
  .should('have.attr', 'type', 'date') 
  .invoke('val', '1995-05-10')
  .trigger('input');
    cy.get('#loginPassword').type('contrasenaSegura');

    cy.get('button[type="submit"]').click();

    
    cy.url().should('include', '/login'); 
  });

  it('debería mostrar errores al dejar los campos vacíos', () => {
    cy.visit(url);
    cy.get('button[type="submit"]').click();

    cy.get('#loginNombre').focus().blur(); 
cy.contains('El nombre es obligatorio').should('be.visible');

  });

   it('debería dar error ya que el email del usuario existe en el sistema', () => {
    cy.visit(url);
    
    cy.get('#loginNombre').type('Juan');
    cy.get('#loginApellidos').type('Pérez');
    cy.get('#loginEmail').type('alberto.hm.99@gmail.com');
    cy.get('#loginTelefono').type('666777888');
    cy.get('#loginNif').type('12345678Z');
    cy.get('#loginDireccion').type('Calle Falsa 123');
    cy.get('#loginFecha')
  .should('have.attr', 'type', 'date') 
  .invoke('val', '1995-05-10')
  .trigger('input');
    cy.get('#loginPassword').type('contrasenaSegura');
    cy.get('button[type="submit"]').click();
    cy.get('.mat-mdc-simple-snack-bar > .mat-mdc-snack-bar-label')
  .invoke('text')
  .should('match', /ya\s*est[aá]\s*en\s*uso/i);


  });
});
