describe('Login Tests', () => {
  
    it('Should show an error message for unsuccessful login', () => {
      cy.intercept('POST', '/user/login', {
        statusCode: 401,
        body: { message: 'Wrong email or password' },
      }).as('postLogin');
  
      cy.visit('http://localhost:3000/login');
  
      cy.get('input[type="email"]').type('wronguser@test.com');
      cy.get('input[type="password"]').type('wrongpassword');
      cy.get('button[type="submit"]').click();
  
      cy.wait('@postLogin');
  
      cy.contains('Wrong email or password').should('be.visible');
    });
  
    it('Should show login successful', () => {
      cy.intercept('POST', '/user/login', {
        statusCode: 200,
        body: { token: 'mockToken', user: { email: 'testuser@test.com' } },
      }).as('postLogin');
  
      cy.visit('http://localhost:3000/login');
  
      cy.get('input[type="email"]').type('testuser@test.com');
      cy.get('input[type="password"]').type('correctpassword');
      cy.get('button[type="submit"]').click();
  
      cy.wait('@postLogin');
  
      cy.contains('Login successful').should('be.visible');
  
    });
  
  });
  