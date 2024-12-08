describe('Signup Tests', () => {

    it('Should display success message for successful signup', () => {
      cy.intercept('POST', '/user/signup', {
        statusCode: 200,
        body: { message: 'User registered successfully' },
      }).as('signUpRequest');
  
      cy.visit('http://localhost:3000/signup');
  
      cy.get('input[type="username"]').type('testuser123');
      cy.get('input[type="email"]').type('testuser123@example.com');
      cy.get('input[type="password"]').type('password123');
      cy.get('button[type="submit"]').click();
  
      cy.wait('@signUpRequest');
  
      cy.contains('Signup successful').should('be.visible');
    });
  
    it('Should display error message from server on failed signup', () => {
      cy.intercept('POST', '/user/signup', {
        statusCode: 400,
        body: 'Invalid user data',
      }).as('signUpRequest');
  
      cy.visit('http://localhost:3000/signup');
  
      cy.get('input[type="username"]').type('testuser123');
      cy.get('input[type="email"]').type('testuser123@example.com');
      cy.get('input[type="password"]').type('password123');
      cy.get('button[type="submit"]').click();
  
      cy.wait('@signUpRequest');
  
      cy.contains('Invalid user data').should('be.visible');
    });
  
    it('Should display error message when no response from server', () => {
      cy.intercept('POST', '/user/signup', {
        forceNetworkError: true,
      }).as('signUpRequest');
  
      cy.visit('http://localhost:3000/signup');
  
      cy.get('input[type="username"]').type('testuser123');
      cy.get('input[type="email"]').type('testuser123@example.com');
      cy.get('input[type="password"]').type('password123');
      cy.get('button[type="submit"]').click();
  
      cy.wait('@signUpRequest');
  
      cy.contains('No response received from server').should('be.visible');
    });
  });
  