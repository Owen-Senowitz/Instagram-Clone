describe('Homepage Test with Mocked Feed API', () => {
  it('should load the homepage and mock the /feed API using fixture', () => {
    cy.fixture('feed.json').then((mockFeed) => {
      cy.intercept('GET', 'http://localhost:8080/feed', {
        statusCode: 200,
        body: mockFeed,
      }).as('getFeed');
    });

    cy.visit('http://localhost:3000');

    cy.wait('@getFeed');

    cy.contains('testuser1');
    cy.contains('This is a test caption');
    
  });
});
