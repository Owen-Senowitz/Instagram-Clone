describe('User should be able to sign up', () => {
    it('should click the button and mock the sign up request', () => {
        cy.intercept('POST', '/user/signup', {
            statusCode: 200,
            body: { message: 'User registered successfully'},
        }).as('signUpRequest');

        cy.visit('http://localhost:3000/signup');
        cy.get('input[type="username"]').type('testuser123');
        cy.get('input[type="email"]').type('testuser123@example.com');
        cy.get('input[type="password"]').type('password123');
        cy.get('button[type="submit"]').click();

        cy.wait('@signUpRequest');

        cy.contains('Signup successful').should('be.visible');
        
    })
})