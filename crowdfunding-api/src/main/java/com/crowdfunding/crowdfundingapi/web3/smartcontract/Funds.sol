//SPDX-License-Identifier: UNLICENSED

pragma solidity >=0.7.0 <0.9.0;
pragma experimental ABIEncoderV2;

contract Funds {

    address public contractOwner;
    constructor() {
        contractOwner = msg.sender;
    }

    event FundsEmition(address sender, address receiver, uint256 amount, uint256 _phaseId, uint256 timestamp);

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
        uint256 _phaseId;
        uint256 amount;
        uint timestamp;
    }

    uint256 public donationsCount;
    mapping (uint256 => FundsStruct) public fundsDonated;
    TransactionStruct[] public transactionHistory;

    function getBalance() public view returns(uint){
        return address(this).balance;
    }

    function depositFunds(address _receiver, uint256 _amount, uint256 _phaseId) external payable{

        require(msg.sender != _receiver,                        "You cannot deposit funds to own collection");
        require(_amount > 0,                                    "Incorrect amount");
        require(fundsDonated[_phaseId].isFraud != true,         "Cannot deposit funds, phase status: fraud");
        require(fundsDonated[_phaseId].isPollEnded != true,     "Cannot deposit funds to ended phase");

        if(fundsDonated[_phaseId].receiver == address(0x0)){
            fundsDonated[_phaseId].receiver = _receiver;
            fundsDonated[_phaseId].amount = _amount;
            fundsDonated[_phaseId].isFraud = false;
            fundsDonated[_phaseId].isPollEnded = false;
            fundsDonated[_phaseId].timestamp = block.timestamp;
        }else{
            fundsDonated[_phaseId].amount += _amount;
        }

        transactionHistory.push(TransactionStruct(
                msg.sender,
                address(this),
                _phaseId,
                _amount,
                block.timestamp
            ));

        emit FundsEmition(
            msg.sender,
            address(this),
            _amount,
            _phaseId,
            block.timestamp
        );

        donationsCount++;
    }

    function getTransactionHiostory() public view returns(TransactionStruct[] memory){
        return transactionHistory;
    }

    function setFraud(uint256 _phaseId) public {
        require(fundsDonated[_phaseId].receiver != address(0x0),   "Invalid phase ID");
        require(msg.sender == contractOwner,                            "Only contract owner can set status of poll");
        require(fundsDonated[_phaseId].isPollEnded == false,       "Poll already ended");
        fundsDonated[_phaseId].isFraud = true;
    }

    function isFraud(uint256 _phaseId) public view returns(bool){
        return fundsDonated[_phaseId].isFraud;
    }

    function isPollEnded(uint256 _phaseId) public view returns(bool){
        return fundsDonated[_phaseId].isPollEnded;
    }

    function setPollEnded(uint256 _phaseId) public {
        require(fundsDonated[_phaseId].receiver != address(0x0),   "Invalid phase ID");
        require(msg.sender == contractOwner,                       "Only contract owner can set status of poll");

        fundsDonated[_phaseId].isPollEnded = true;
    }

    function sendFundsToOwner(address payable _toReceive, uint256 _phaseId) public {

        require(fundsDonated[_phaseId].receiver != address(0x0),"Invalid phase ID");
        require(msg.sender == fundsDonated[_phaseId].receiver,  "Only the collection owner can withdraw funds");
        require(_toReceive == fundsDonated[_phaseId].receiver,  "Only the collection owner can withdraw funds");
        require(fundsDonated[_phaseId].amount > 0,              "Insufficient funds");
        require(fundsDonated[_phaseId].isPollEnded == true,     "Poll has not end");
        require(fundsDonated[_phaseId].isFraud == false,        "Phase status is fraud. You cannot withdraw funds!");

        emit FundsEmition(
            address(this),
            fundsDonated[_phaseId].receiver,
            fundsDonated[_phaseId].amount,
            _phaseId,
            block.timestamp
        );

        transactionHistory.push(TransactionStruct(
                address(this),
                fundsDonated[_phaseId].receiver,
                _phaseId,
                fundsDonated[_phaseId].amount,
                block.timestamp
            ));

        _toReceive.transfer(fundsDonated[_phaseId].amount);
        fundsDonated[_phaseId].amount = 0;
    }

    function sendFundsToDonators(address payable _toReceive, uint256 _phaseId, uint256[] memory _transactionArray) public{

        require(fundsDonated[_phaseId].receiver != address(0x0),   "Invalid phase ID");
        require(fundsDonated[_phaseId].isPollEnded == true,         "Poll has not end");
        require(fundsDonated[_phaseId].isFraud == true,             "Phase status is not fraud. You cannot withdraw funds");

        uint256 amountToReturn = 0;
        for(uint256 i = 0; i < _transactionArray.length; i++){

            uint256 _transactionHistoryId = _transactionArray[i];
            if (transactionHistory[_transactionHistoryId].amount == 0) continue;
            require(msg.sender == transactionHistory[_transactionHistoryId].sender,             "Only the funds owner can withdraw funds");
            require(transactionHistory[_transactionHistoryId].sender == _toReceive,             "Invalid receiver address");
            require(transactionHistory[_transactionHistoryId]._phaseId == _phaseId,   "Incorrect phase ID");

            amountToReturn += transactionHistory[_transactionHistoryId].amount;
            transactionHistory[_transactionHistoryId].amount = 0;
        }

        require(amountToReturn != 0, "No funds to be refunded");

        emit FundsEmition(
            address(this),
            _toReceive,
            amountToReturn,
            _phaseId,
            block.timestamp
        );

        _toReceive.transfer(amountToReturn);
        fundsDonated[_phaseId].amount -= amountToReturn;
    }
}