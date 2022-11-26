//SPDX-License-Identifier: UNLICENSED

pragma solidity >=0.7.0 <0.9.0;
//pragma solidity ^ 0.8.9;
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

    function getCommissionRate(address _receiver, uint256 _amount, string memory _collectionType) public returns(uint256){

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

contract Funds is Commission{

    event FundsEmition(address sender, address receiver, uint256 amount,
        uint256 _phaseId, uint256 timestamp);

    struct FundsStruct {
        address receiver;
        uint256 amount;
        uint256 timestamp;
    }

    struct TransactionStruct {
        address sender;
        address receiver;
        uint256 phaseId;
        uint256 amount;
        uint timestamp;
    }

    uint256 public donationsCount;
    mapping (uint256 => FundsStruct) public fundsDonated;
    TransactionStruct[] transactionHistory;

    function depositFunds(address _receiver, uint _amount, uint256 _phaseId, string memory _collectionType) external payable{

        uint256 _amountLeft = _amount - Commission.getCommissionRate(_receiver, _amount, _collectionType);

        if(fundsDonated[_phaseId].receiver == address(0x0)){
            fundsDonated[_phaseId].receiver = _receiver;
            fundsDonated[_phaseId].amount = _amountLeft;
            fundsDonated[_phaseId].timestamp = block.timestamp;
        }else{
            fundsDonated[_phaseId].amount += _amountLeft;
        }

        transactionHistory.push(TransactionStruct(
                msg.sender,
                address(this),
                _phaseId,
                _amountLeft,
                block.timestamp
            ));

        emit FundsEmition(
            msg.sender,
            address(this),
            _amountLeft,
            _phaseId,
            block.timestamp
        );

        donationsCount++;
    }

    function getAllDonatedFunds() public view returns (FundsStruct[] memory){

        FundsStruct[] memory temp = new FundsStruct[](donationsCount);
        for (uint i = 0; i < donationsCount; i++){
            temp[i] = fundsDonated[i];
        }

        return temp;
    }

    function getTransactionHiostory() public view returns(TransactionStruct[] memory){
        return transactionHistory;
    }

    function sendFundsToOwner(uint256 _phaseId) public payable{

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

        fundsDonated[_phaseId].amount = 0;
    }

    function sendFundsToDonators(uint256 _phaseId, address donatorAddress, uint256 amountToReturn) public{

        emit FundsEmition(
            address(this),
            donatorAddress,
            amountToReturn,
            _phaseId,
            block.timestamp
        );

        fundsDonated[_phaseId].amount -= amountToReturn;
    }
}