describe('Pruebas de la página de reservas', () => {

  beforeEach(() => {
   
    cy.visit('http://localhost:4200/reservas'); 
  });




  it('Debe mostrar un botón para iniciar sesión cuando el email está vacío', () => {
    cy.get('p').contains('Inicie sesión o regístrese');
    cy.get('button').contains('Iniciar sesión / Registrarse');
  });

  it('Debe mostrar el calendario de reservas cuando el email está presente', () => {
   
    cy.visit('http://localhost:4200/login'); // Visita la página

// Simula un login
cy.get('#loginEmail').type('alberto.hm.99@gmail.com');
    cy.get('#loginPassword').type('alberto12345');
    cy.get('button[type="submit"]').click();
    cy.wait(2000); // Espera 2 segundos para que se complete la redirección
cy.visit('http://localhost:4200/reservas'); 
cy.get('.reservas-card').should('be.visible');


  
   
  });

   it('Debe mostrar el tiempo para las fechas seleccionadas', () => {
   
     cy.visit('http://localhost:4200/login'); // Visita la página

// Simula un login
cy.get('#loginEmail').type('alberto.hm.99@gmail.com');
    cy.get('#loginPassword').type('alberto12345');
    cy.get('button[type="submit"]').click();
    cy.wait(2000); // Espera 2 segundos para que se complete la redirección
cy.visit('http://localhost:4200/reservas'); 


    cy.wait(2000); // Espera 2 segundos para que se complete la redirección

cy.get('.reservas-card').should('be.visible');
  

 cy.get('.calendar-controls > :nth-child(3) > .mdc-button__label').click();
  cy.get(':nth-child(2) > .cal-cell-row > :nth-child(2) > .cal-cell-top').click();
  cy.get(':nth-child(2) > .cal-cell-row > :nth-child(3) > .cal-cell-top').click();
  cy.get('.weather-card')
  .scrollIntoView(); // Desplazar la vista hasta que el elemento sea visible

   cy.get('.weather-card').should('be.visible');
  });

   it('Debe mostrar el formulario para completar la reserva rellenado desde el backend', () => {
   
     cy.visit('http://localhost:4200/login'); // Visita la página

// Simula un login
cy.get('#loginEmail').type('alberto.hm.99@gmail.com');
    cy.get('#loginPassword').type('alberto12345');
    cy.get('button[type="submit"]').click();
    cy.wait(2000); // Espera 2 segundos para que se complete la redirección
cy.visit('http://localhost:4200/reservas'); 


    cy.wait(2000); // Espera 2 segundos para que se complete la redirección

cy.get('.reservas-card').should('be.visible');
  

 cy.get('.calendar-controls > :nth-child(3) > .mdc-button__label').click();
  cy.get(':nth-child(2) > .cal-cell-row > :nth-child(2) > .cal-cell-top').click();
  cy.get(':nth-child(2) > .cal-cell-row > :nth-child(3) > .cal-cell-top').click();
   
    cy.get(':nth-child(2) > .cal-cell-row > :nth-child(3) > .cal-events > .cal-event').click();

    cy.get('.mat-mdc-form-field.ng-tns-c508571215-8 > .mat-mdc-text-field-wrapper > .mat-mdc-form-field-flex > .mat-mdc-form-field-infix')
  .find('input')  // Suponiendo que sea un campo de entrada dentro del contenedor
  .should('not.have.value', '');  // Verifica que el campo no esté vacío

});

});

