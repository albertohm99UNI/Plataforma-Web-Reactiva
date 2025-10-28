describe('LoginComponent', () => {
  it('debería iniciar sesión con credenciales válidas', () => {
    cy.visit('http://localhost:4200/login');

    cy.get('#loginEmail').type('alberto.hm.99@gmail.com');
    cy.get('#loginPassword').type('alberto12345');
    cy.get('button[type="submit"]').click();

    // Verifica que redirige a /inicio tras el login
    cy.url().should('include', '/inicio');
  });

  it('debería mostrar error si los datos son inválidos', () => {
    cy.visit('http://localhost:4200/login');

   cy.get('#loginEmail').type('usuarioPrueba');
    cy.get('#loginPassword').type('contraseña123');
    cy.get('button[type="submit"]').click();

    cy.contains('El usuario o contraseña no existen').should('be.visible');
  });
});
