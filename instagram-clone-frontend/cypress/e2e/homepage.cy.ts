describe('Homepage Test with Mocked Feed API', () => {
  it('should load the homepage and mock the /feed API using fixture', () => {
    // Load the fixture file
    cy.fixture('feed.json').then((mockFeed) => {
      // Mock the /feed API response
      cy.intercept('GET', 'http://localhost:8080/feed', {
        statusCode: 200,
        body: mockFeed,
      }).as('getFeed');
    });

    // Visit the homepage
    cy.visit('http://localhost:3000');

    // Wait for the /feed API to be called
    cy.wait('@getFeed');

    // Verify the mocked API response data is rendered on the page
    cy.contains('testuser1');
    cy.contains('This is a test caption');
    
  });
});
