import "@cypress/code-coverage/support";
before(() => {
	cy.task("coverage", {}).then((coverage) => {
		console.log("Coverage data:", coverage);
	});
});

after(() => {
	cy.task('coverageReport'); // Automatically generate the report after all tests
  });
