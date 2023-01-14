package com.crowdfunding.crowdfundingapi.web3.advertise;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.StaticStruct;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.RemoteFunctionCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple3;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

@SuppressWarnings("rawtypes")
public class Advertise extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50600080546001600160a01b03191633179055610c0c806100326000396000f3fe6080604052600436106100915760003560e01c80636b408132116100595780636b4081321461015857806391c589d51461016d578063b538d7a11461019c578063ce606ee0146101be578063fa09e630146101e057610091565b8063051a6d951461009657806312065fe0146100b8578063233d7312146100e3578063243e7a87146101125780634b41200a14610145575b600080fd5b3480156100a257600080fd5b506100b66100b13660046108b0565b610200565b005b3480156100c457600080fd5b506100cd610284565b6040516100da9190610bcd565b60405180910390f35b3480156100ef57600080fd5b506101036100fe36600461095a565b610288565b6040516100da939291906109f2565b34801561011e57600080fd5b5061013261012d36600461095a565b6102b4565b6040516100da97969594939291906109b4565b6100b6610153366004610972565b61030d565b34801561016457600080fd5b506100cd6105f0565b34801561017957600080fd5b5061018d61018836600461095a565b6105f6565b6040516100da93929190610aa5565b3480156101a857600080fd5b506101b16106a3565b6040516100da9190610a13565b3480156101ca57600080fd5b506101d3610750565b6040516100da91906109a0565b3480156101ec57600080fd5b506100b66101fb366004610882565b61075f565b6000546001600160a01b031633146102335760405162461bcd60e51b815260040161022a90610b51565b60405180910390fd5b60015460009081526003602090815260409091208451610255928601906107ef565b506001805460009081526003602052604080822083019490945581548152929092206002015580548101905550565b4790565b60026020819052600091825260409091208054600182015491909201546001600160a01b039092169183565b600481815481106102c157fe5b600091825260209091206007909102018054600182015460028301546003840154600485015460058601546006909601546001600160a01b039586169750949093169491939092919087565b60008181526003602052604090206002015461033b5760405162461bcd60e51b815260040161022a90610ba0565b6000828152600260205260409020546001600160a01b031615801590610371575060008281526002602052604090206001015442105b156103c057600082815260026020818152604080842080546001600160a01b03191633178155808401869055858552600383529084208301549386905291905260010180549091019055610406565b600082815260026020818152604080842080546001600160a01b031916331781558084018690558585526003835290842083015493869052919052429091016001909101555b6040805160e081018252338082523060208084018281528486018881526000888152600384528781206001908101805460608a0190815260808a018c81528d855260028089528c86208501805460a08e019081524260c08f0181815260048054998a0181558a529e517f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19b600790990298890180546001600160a01b03199081166001600160a01b03938416179091559b517f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19c8a018054909d16911617909a5597517f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19d87015592517f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19e86015590517f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd19f85015594517f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd1a084015598517f8a35acfbc15ff81a39ae7d344fd709f28e8600b4aa8c65c6b64bfe7fe36bd1a19092019190915554908a90529252935494517ffbb4b689ec1e9d1f773479bdf595a5274e5e1d60f6e06ac5c71da85d9ac4d4d7956105e495899392899291906109b4565b60405180910390a15050565b60015481565b60036020908152600091825260409182902080548351601f6002600019610100600186161502019093169290920491820184900484028101840190945280845290929183919083018282801561068d5780601f106106625761010080835404028352916020019161068d565b820191906000526020600020905b81548152906001019060200180831161067057829003601f168201915b5050505050908060010154908060020154905083565b60606004805480602002602001604051908101604052809291908181526020016000905b828210156107475760008481526020908190206040805160e0810182526007860290920180546001600160a01b039081168452600180830154909116848601526002820154928401929092526003810154606084015260048101546080840152600581015460a08401526006015460c083015290835290920191016106c7565b50505050905090565b6000546001600160a01b031681565b6000546001600160a01b031633146107895760405162461bcd60e51b815260040161022a90610b07565b6000546001600160a01b038281169116146107b65760405162461bcd60e51b815260040161022a90610b07565b6040516001600160a01b038216904780156108fc02916000818181858888f193505050501580156107eb573d6000803e3d6000fd5b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061083057805160ff191683800117855561085d565b8280016001018555821561085d579182015b8281111561085d578251825591602001919060010190610842565b5061086992915061086d565b5090565b5b80821115610869576000815560010161086e565b600060208284031215610893578081fd5b81356001600160a01b03811681146108a9578182fd5b9392505050565b6000806000606084860312156108c4578182fd5b833567ffffffffffffffff808211156108db578384fd5b818601915086601f8301126108ee578384fd5b8135818111156108fc578485fd5b6040516020601f8301601f191682018101848111838210171561091d578788fd5b60405282825284830181018a1015610933578687fd5b82818601828401379181018201959095529397938601359650505060409093013592509050565b60006020828403121561096b578081fd5b5035919050565b60008060408385031215610984578182fd5b50508035926020909101359150565b6001600160a01b03169052565b6001600160a01b0391909116815260200190565b6001600160a01b03978816815295909616602086015260408501939093526060840191909152608083015260a082015260c081019190915260e00190565b6001600160a01b039390931683526020830191909152604082015260600190565b602080825282518282018190526000919060409081850190868401855b82811015610a98578151610a45858251610993565b86810151610a5588870182610993565b508086015185870152606080820151908601526080808201519086015260a0808201519086015260c0908101519085015260e09093019290850190600101610a30565b5091979650505050505050565b6000606082528451806060840152815b81811015610ad25760208188018101516080868401015201610ab5565b81811115610ae35782608083860101525b5060208301949094525060408101919091526080601f909201601f19160101919050565b6020808252602a908201527f4f6e6c792074686520636f6e7472616374206f776e65722063616e2077697468604082015269647261772066756e647360b01b606082015260800190565b6020808252602f908201527f4f6e6c792074686520636f6e7472616374206f776e65722063616e206164642060408201526e61647665727469736520747970657360881b606082015260800190565b602080825260139082015272151e5c1948191bd95cc81b9bdd08195e1a5cdd606a1b604082015260600190565b9081526020019056fea2646970667358221220a66555e52fcb7e8646f0fb1d0741403c524ef8de6f5ffadd7b9336cba02a325364736f6c63430007010033";

    public static final String FUNC_ADDADTYPE = "addAdType";

    public static final String FUNC_ADVERTISEBOUGHT = "advertiseBought";

    public static final String FUNC_ADVERTISETYPE = "advertiseType";

    public static final String FUNC_AVAILABLEADPLANSID = "availableAdPlansId";

    public static final String FUNC_BUYADVERTISEMENT = "buyAdvertisement";

    public static final String FUNC_CONTRACTOWNER = "contractOwner";

    public static final String FUNC_GETBALANCE = "getBalance";

    public static final String FUNC_GETTRANSACTIONHIOSTORY = "getTransactionHiostory";

    public static final String FUNC_TRANSACTIONHISTORY = "transactionHistory";

    public static final String FUNC_WITHDRAWALL = "withdrawAll";

    public static final Event ADVERTISELOG_EVENT = new Event("AdvertiseLog",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected Advertise(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Advertise(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Advertise(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Advertise(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<AdvertiseLogEventResponse> getAdvertiseLogEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(ADVERTISELOG_EVENT, transactionReceipt);
        ArrayList<AdvertiseLogEventResponse> responses = new ArrayList<AdvertiseLogEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            AdvertiseLogEventResponse typedResponse = new AdvertiseLogEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.receiver = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse._collectionId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse._adType = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse._promoTo = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
            typedResponse._timestamp = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AdvertiseLogEventResponse> advertiseLogEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AdvertiseLogEventResponse>() {
            @Override
            public AdvertiseLogEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(ADVERTISELOG_EVENT, log);
                AdvertiseLogEventResponse typedResponse = new AdvertiseLogEventResponse();
                typedResponse.log = log;
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.receiver = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse._collectionId = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse._adType = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse._promoTo = (BigInteger) eventValues.getNonIndexedValues().get(5).getValue();
                typedResponse._timestamp = (BigInteger) eventValues.getNonIndexedValues().get(6).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AdvertiseLogEventResponse> advertiseLogEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ADVERTISELOG_EVENT));
        return advertiseLogEventFlowable(filter);
    }

    public RemoteFunctionCall<TransactionReceipt> addAdType(String _adName, BigInteger _price, BigInteger _duration) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_ADDADTYPE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(_adName),
                        new org.web3j.abi.datatypes.generated.Uint256(_price),
                        new org.web3j.abi.datatypes.generated.Uint256(_duration)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>> advertiseBought(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ADVERTISEBOUGHT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>>(function,
                new Callable<Tuple3<String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple3<String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>> advertiseType(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ADVERTISETYPE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple3<String, BigInteger, BigInteger>>(function,
                new Callable<Tuple3<String, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple3<String, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple3<String, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue());
                    }
                });
    }

    public RemoteFunctionCall<BigInteger> availableAdPlansId() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_AVAILABLEADPLANSID,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> buyAdvertisement(BigInteger _collectionId, BigInteger _adTypeId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_BUYADVERTISEMENT,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_collectionId),
                        new org.web3j.abi.datatypes.generated.Uint256(_adTypeId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<String> contractOwner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CONTRACTOWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<BigInteger> getBalance() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETBALANCE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<List> getTransactionHiostory() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETTRANSACTIONHIOSTORY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<TransactionStruct>>() {}));
        return new RemoteFunctionCall<List>(function,
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
    }

    public RemoteFunctionCall<Tuple7<String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>> transactionHistory(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TRANSACTIONHISTORY,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple7<String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple7<String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple7<String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<String, String, BigInteger, BigInteger, BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (BigInteger) results.get(3).getValue(),
                                (BigInteger) results.get(4).getValue(),
                                (BigInteger) results.get(5).getValue(),
                                (BigInteger) results.get(6).getValue());
                    }
                });
    }

    public RemoteFunctionCall<TransactionReceipt> withdrawAll(String _to) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_WITHDRAWALL,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _to)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static Advertise load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Advertise(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Advertise load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Advertise(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Advertise load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Advertise(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Advertise load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Advertise(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Advertise> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Advertise.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Advertise> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Advertise.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Advertise> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Advertise.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Advertise> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Advertise.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class TransactionStruct extends StaticStruct {
        public String sender;

        public String receiver;

        public BigInteger _collectionId;

        public BigInteger amount;

        public BigInteger _adType;

        public BigInteger _promoTo;

        public BigInteger timestamp;

        public TransactionStruct(String sender, String receiver, BigInteger _collectionId, BigInteger amount, BigInteger _adType, BigInteger _promoTo, BigInteger timestamp) {
            super(new org.web3j.abi.datatypes.Address(sender),new org.web3j.abi.datatypes.Address(receiver),new org.web3j.abi.datatypes.generated.Uint256(_collectionId),new org.web3j.abi.datatypes.generated.Uint256(amount),new org.web3j.abi.datatypes.generated.Uint256(_adType),new org.web3j.abi.datatypes.generated.Uint256(_promoTo),new org.web3j.abi.datatypes.generated.Uint256(timestamp));
            this.sender = sender;
            this.receiver = receiver;
            this._collectionId = _collectionId;
            this.amount = amount;
            this._adType = _adType;
            this._promoTo = _promoTo;
            this.timestamp = timestamp;
        }

        public TransactionStruct(Address sender, Address receiver, Uint256 _collectionId, Uint256 amount, Uint256 _adType, Uint256 _promoTo, Uint256 timestamp) {
            super(sender,receiver,_collectionId,amount,_adType,_promoTo,timestamp);
            this.sender = sender.getValue();
            this.receiver = receiver.getValue();
            this._collectionId = _collectionId.getValue();
            this.amount = amount.getValue();
            this._adType = _adType.getValue();
            this._promoTo = _promoTo.getValue();
            this.timestamp = timestamp.getValue();
        }
    }

    public static class AdvertiseLogEventResponse extends BaseEventResponse {
        public String sender;

        public String receiver;

        public BigInteger _collectionId;

        public BigInteger amount;

        public BigInteger _adType;

        public BigInteger _promoTo;

        public BigInteger _timestamp;
    }
}
