//SPDX-License-Identifier: UNLICENSED

pragma solidity >=0.7.0 <0.9.0;
//pragma solidity ^ 0.8.9;
pragma experimental ABIEncoderV2;

contract Funds {

    address public contractOwner;
    constructor() {
        contractOwner = msg.sender;
    }

    event FundsEmition(address sender, address receiver, uint256 amount, uint256 _collectionId, uint256 timestamp);

    struct FundsStruct {
        address receiver;
        uint256 amount;
        bool isFraud;
        bool isPollEnded;
        uint256 timestamp;
    }

    struct TransactionStruct {
        address sender;
        address receiver;
        uint256 _collectionId;
        uint256 amount;
        uint timestamp;
    }

    uint256 public donationsCount;
    mapping (uint256 => FundsStruct) public fundsDonated;
    TransactionStruct[] public transactionHistory;

    function getBalance() public view returns(uint){
        return address(this).balance;
    }

    function depositFunds(address _receiver, uint256 _amount, uint256 _collectionId) external payable{

        require(msg.sender != _receiver,                        "You cannot deposit funds to own collection");
        require(_amount > 0,                                    "Incorrect amount");
        require(fundsDonated[_collectionId].isFraud != true,    "Cannot deposit funds, collection status: fraud");
        require(fundsDonated[_collectionId].isPollEnded != true,"Cannot deposit funds to ended collection");

        if(fundsDonated[_collectionId].receiver == address(0x0)){
            fundsDonated[_collectionId].receiver = _receiver;
            fundsDonated[_collectionId].amount = _amount;
            fundsDonated[_collectionId].isFraud = false;
            fundsDonated[_collectionId].isPollEnded = false;
            fundsDonated[_collectionId].timestamp = block.timestamp;
        }else{
            fundsDonated[_collectionId].amount += _amount;
        }

        transactionHistory.push(TransactionStruct(
                msg.sender,
                address(this),
                _collectionId,
                _amount,
                block.timestamp
            ));

        emit FundsEmition(
            msg.sender,
            address(this),
            _amount,
            _collectionId,
            block.timestamp
        );

        donationsCount++;
    }

    function getTransactionHiostory() public view returns(TransactionStruct[] memory){
        return transactionHistory;
    }

    function setFraud(uint256 _collectionId) public {
        require(fundsDonated[_collectionId].receiver != address(0x0),   "Invalid collection ID");
        require(msg.sender == contractOwner,                            "Only contract owner can set status of poll");
        require(fundsDonated[_collectionId].isPollEnded == false,       "Poll already ended");
        fundsDonated[_collectionId].isFraud = true;
    }

    function isFraud(uint256 _collectionId) public view returns(bool){
        return fundsDonated[_collectionId].isFraud;
    }

    function isPollEnded(uint256 _collectionId) public view returns(bool){
        return fundsDonated[_collectionId].isPollEnded;
    }

    function setPollEnded(uint256 _collectionId) public {
        require(fundsDonated[_collectionId].receiver != address(0x0),   "Invalid collection ID");
        require(msg.sender == contractOwner,                            "Only contract owner can set status of poll");

        fundsDonated[_collectionId].isPollEnded = true;
    }

    function sendFundsToOwner(address payable _toReceive, uint256 _collectionId) public {

        require(fundsDonated[_collectionId].receiver != address(0x0),"Invalid collection ID");
        require(msg.sender == fundsDonated[_collectionId].receiver,  "Only the collection owner can withdraw funds");
        require(_toReceive == fundsDonated[_collectionId].receiver,  "Only the collection owner can withdraw funds");
        require(fundsDonated[_collectionId].amount > 0,              "Insufficient funds");
        require(fundsDonated[_collectionId].isPollEnded == true,     "Poll has not end");
        require(fundsDonated[_collectionId].isFraud == false,        "Collection status is fraud. You cannot withdraw funds!");

        emit FundsEmition(
            address(this),
            fundsDonated[_collectionId].receiver,
            fundsDonated[_collectionId].amount,
            _collectionId,
            block.timestamp
        );

        transactionHistory.push(TransactionStruct(
                address(this),
                fundsDonated[_collectionId].receiver,
                _collectionId,
                fundsDonated[_collectionId].amount,
                block.timestamp
            ));

        _toReceive.transfer(fundsDonated[_collectionId].amount);
        fundsDonated[_collectionId].amount = 0;
    }

    function sendFundsToDonators(address payable _toReceive, uint256 _collectionId, uint256[] memory _transactionArray) public{

        require(fundsDonated[_collectionId].receiver != address(0x0),   "Invalid collection ID");
        require(fundsDonated[_collectionId].isPollEnded == true, "Poll has not end");
        require(fundsDonated[_collectionId].isFraud == true, "Collection status is not fraud. You cannot withdraw funds");

        uint256 amountToReturn = 0;
        for(uint256 i = 0; i < _transactionArray.length; i++){

            uint256 _transactionHistoryId = _transactionArray[i];
            if (transactionHistory[_transactionHistoryId].amount == 0) continue;
            require(msg.sender == transactionHistory[_transactionHistoryId].sender, "Only the funds owner can withdraw funds");
            require(transactionHistory[_transactionHistoryId].sender == _toReceive, "Invalid receiver address");
            require(transactionHistory[_transactionHistoryId]._collectionId == _collectionId,  "Incorrect Collection ID");

            amountToReturn += transactionHistory[_transactionHistoryId].amount;
            transactionHistory[_transactionHistoryId].amount = 0;
        }

        require(amountToReturn != 0, "No funds to be refunded");

        emit FundsEmition(
            address(this),
            _toReceive,
            amountToReturn,
            _collectionId,
            block.timestamp
        );

        _toReceive.transfer(amountToReturn);
        fundsDonated[_collectionId].amount -= amountToReturn;
    }
}