const { defineConfig } = require('cypress');

module.exports = defineConfig({
  e2e: {
    setupNodeEvents(on, config) {
      // Add any Cypress plugins or tasks here
      return config;
    },
    baseUrl: 'http://localhost:3000', // Adjust as needed
  },
});
