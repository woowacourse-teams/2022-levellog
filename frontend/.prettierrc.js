module.exports = {
  printWidth: 100,
  singleQuote: true,
  trailingComma: 'all',
  endOfLine: 'auto',
  importOrder: [
    '^((?!/).)*$',
    '^(@|components)(.*)$',
    '^(pages)(.*)$',
    '^(hooks)(.*|$)',
    '^(api)(.*|$)',
    '^(styles)(.*)$',
    '^(routes)(.*)$',
    '^(constants)(.*)$',
  ],
  importOrderSeparation: true,
  importOrderSortSpecifiers: true,
};
