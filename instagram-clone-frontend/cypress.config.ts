import { defineConfig } from 'cypress';
import codeCoverageTask from '@cypress/code-coverage/task';

export default defineConfig({
  e2e: {
    supportFile: 'cypress/support/index.ts',
    setupNodeEvents(on, config) {
      codeCoverageTask(on, config); // Add the code coverage tasks
      on('task', {
        coverage(coverage) {
          console.log('Received coverage data:', coverage);
          return null; // Add custom logic here if needed
        },
      });
      return config;
    },
    baseUrl: 'http://localhost:3000', // Ensure this matches your app's dev server
  },
});
