//SPDX-License-Identifier: UNLICENSED

pragma solidity >=0.7.0 <0.9.0;
pragma experimental ABIEncoderV2;

contract Commission {

    event CommissionEvent(address sender, address receiver, uint256 _amount, uint256 comission, uint256 timestamp);

    struct CommissionStruct {
        address sender;
        address receiver;
        uint256 amount;
        uint256 commission;
        uint256 timestamp;
    }

    CommissionStruct[] commissionsStructArray;
    function payCommission(address _receiver, uint256 _amount, uint256 _commission) public{

        commissionsStructArray.push(CommissionStruct(
                msg.sender,
                _receiver,
                _amount,
                _commission,
                block.timestamp
            ));

        emit CommissionEvent(
            msg.sender,
            _receiver,
            _amount,
            _commission,
            block.timestamp
        );

    }

    function getCommissionRate(address _receiver, uint256 _amount, string memory _collectionType) public payable returns(uint256){

        uint256 commission = 0;
        if(keccak256(abi.encodePacked('CHARITY')) == keccak256(abi.encodePacked(_collectionType)) ){
            return commission;
        }

        if(_amount < 50000000000){//50ETH
            commission = 5000000;
        } else if(_amount > 50000000000 && _amount < 100000000000) {//50-100
            commission = 10000000;
        } else if(_amount > 100000000000 && _amount < 500000000000){//100-500
            commission = 50000000;
        } else {//above 500ETH
            commission = 500000000;
        }

        payCommission(_receiver, _amount , commission);

        return commission;
    }
}