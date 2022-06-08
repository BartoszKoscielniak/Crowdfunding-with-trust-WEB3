require('@nomiclabs/hardhat-waffle');

module.exports = {
  solidity: '0.8.0',
  networks: {
    ropsten: {
      url: 'https://eth-ropsten.alchemyapi.io/v2/wRMli82yoNDMSnnnqGD90Xi3ti778o1L',
      accounts: [ '716da35b19c81304b6eaf5d5b94c69cdde762c6be978817613fc8e05216c6d03' ]
    }
  }
}
