const { override } = require('customize-cra');
const babelPluginIstanbul = require('babel-plugin-istanbul');

module.exports = override((config) => {
  config.module.rules[1].oneOf.forEach((rule) => {
    if (rule.loader && rule.loader.includes('babel-loader')) {
      rule.options.plugins = (rule.options.plugins || []).concat(babelPluginIstanbul);
    }
  });
  return config;
});
