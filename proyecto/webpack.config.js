const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');



module.exports = {
  // ... otras configuraciones de Webpack

  plugins: [
    // Inyecta variables de entorno en la aplicación
    new webpack.DefinePlugin({
      'process.env.baseURL': JSON.stringify(process.env.BASE_URL || 'http://localhost:8080/api/v1/'),
    }),
    new HtmlWebpackPlugin({
      template: './src/index.html',
      filename: 'index.html',
      meta: {
        charset: 'UTF-8',  // Asegura que se use UTF-8
      },
    })
  ]
};
