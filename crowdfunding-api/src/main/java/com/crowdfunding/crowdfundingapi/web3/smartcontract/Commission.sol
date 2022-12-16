//SPDX-License-Identifier: UNLICENSED

pragma solidity >=0.7.0 <0.9.0;
pragma experimental ABIEncoderV2;

contract Commission {

    uint256 public commissionRate;
    address public contractOwner;

    constructor() {
        contractOwner = msg.sender;
        commissionRate = 1000;
    }

    function getBalance() public view returns(uint){
        return address(this).balance;
    }

    function withdrawAll(address payable _to) public {
        require(contractOwner == msg.sender, "Only the contract owner can withdraw funds");
        require(contractOwner == _to, "Only the contract owner can withdraw funds");
        _to.transfer(address(this).balance);
    }

    event CommissionEvent(address sender, uint256 _amount, uint256 comission, uint256 timestamp);

    struct CommissionStruct {
        address sender;
        uint256 amount;
        uint256 commission;
        uint256 timestamp;
    }

    CommissionStruct[] commissionsStructArray;
    function payCommission(uint256 _amount, uint256 _commission) public payable{
        commissionsStructArray.push(CommissionStruct(
            msg.sender,
            _amount,
            _commission,
            block.timestamp
        ));

        emit CommissionEvent(
            msg.sender,
            _amount,
            _commission,
            block.timestamp
        );
    }

    function getCommissionRate(uint256 _amount, string memory _collectionType) public view returns(uint256){

        uint256 commission = 0;
        if(keccak256(abi.encodePacked('CHARITY')) == keccak256(abi.encodePacked(_collectionType)) ){
            return commission;
        }

        if(_amount > commissionRate){
            commission = _amount / commissionRate;
        }

        return commission;
    }
}