package com.crowdfunding.crowdfundingapi.web3.wrappedcontracts;

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
import org.web3j.abi.datatypes.Event;
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
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.8.7.
 */
@SuppressWarnings("rawtypes")
public class Advertise extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50600080546001600160a01b03191633179055610852806100326000396000f3fe60806040526004361061007b5760003560e01c80636b4081321161004e5780636b4081321461010f57806391c589d514610124578063ce606ee014610153578063fa09e630146101755761007b565b8063051a6d951461008057806312065fe0146100a2578063233d7312146100cd5780634b41200a146100fc575b600080fd5b34801561008c57600080fd5b506100a061009b366004610579565b610195565b005b3480156100ae57600080fd5b506100b7610219565b6040516100c49190610813565b60405180910390f35b3480156100d957600080fd5b506100ed6100e8366004610623565b61021d565b6040516100c49392919061069e565b6100a061010a36600461063b565b610249565b34801561011b57600080fd5b506100b7610366565b34801561013057600080fd5b5061014461013f366004610623565b61036c565b6040516100c4939291906106bf565b34801561015f57600080fd5b50610168610419565b6040516100c4919061065c565b34801561018157600080fd5b506100a061019036600461054b565b610428565b6000546001600160a01b031633146101c85760405162461bcd60e51b81526004016101bf9061076b565b60405180910390fd5b600154600090815260036020908152604090912084516101ea928601906104b8565b506001805460009081526003602052604080822083019490945581548152929092206002015580548101905550565b4790565b60026020819052600091825260409091208054600182015491909201546001600160a01b039092169183565b60008181526003602052604090206001015434146102795760405162461bcd60e51b81526004016101bf906107e7565b6000818152600360205260409020600201546102a75760405162461bcd60e51b81526004016101bf906107ba565b60008181526003602090815260408083206002908101548685529252909120546001600160a01b0316156102f1576000838152600260205260409020600101805482019055610322565b600083815260026020819052604090912080546001600160a01b031916331781559081018390554282016001909101555b7fdce70202cd9a7d5a779c9b0ecca303ad168dce51764096f6a90de0d8d5bac2bd3384848442604051610359959493929190610670565b60405180910390a1505050565b60015481565b60036020908152600091825260409182902080548351601f600260001961010060018616150201909316929092049182018490048402810184019094528084529092918391908301828280156104035780601f106103d857610100808354040283529160200191610403565b820191906000526020600020905b8154815290600101906020018083116103e657829003601f168201915b5050505050908060010154908060020154905083565b6000546001600160a01b031681565b6000546001600160a01b031633146104525760405162461bcd60e51b81526004016101bf90610721565b6000546001600160a01b0382811691161461047f5760405162461bcd60e51b81526004016101bf90610721565b6040516001600160a01b038216904780156108fc02916000818181858888f193505050501580156104b4573d6000803e3d6000fd5b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106104f957805160ff1916838001178555610526565b82800160010185558215610526579182015b8281111561052657825182559160200191906001019061050b565b50610532929150610536565b5090565b5b808211156105325760008155600101610537565b60006020828403121561055c578081fd5b81356001600160a01b0381168114610572578182fd5b9392505050565b60008060006060848603121561058d578182fd5b833567ffffffffffffffff808211156105a4578384fd5b818601915086601f8301126105b7578384fd5b8135818111156105c5578485fd5b6040516020601f8301601f19168201810184811183821017156105e6578788fd5b60405282825284830181018a10156105fc578687fd5b82818601828401379181018201959095529397938601359650505060409093013592509050565b600060208284031215610634578081fd5b5035919050565b6000806040838503121561064d578182fd5b50508035926020909101359150565b6001600160a01b0391909116815260200190565b6001600160a01b03959095168552602085019390935260408401919091526060830152608082015260a00190565b6001600160a01b039390931683526020830191909152604082015260600190565b6000606082528451806060840152815b818110156106ec57602081880181015160808684010152016106cf565b818111156106fd5782608083860101525b5060208301949094525060408101919091526080601f909201601f19160101919050565b6020808252602a908201527f4f6e6c792074686520636f6e7472616374206f776e65722063616e2077697468604082015269647261772066756e647360b01b606082015260800190565b6020808252602f908201527f4f6e6c792074686520636f6e7472616374206f776e65722063616e206164642060408201526e61647665727469736520747970657360881b606082015260800190565b602080825260139082015272151e5c1948191bd95cc81b9bdd08195e1a5cdd606a1b604082015260600190565b602080825260129082015271496e73756666696369656e742066756e647360701b604082015260600190565b9081526020019056fea26469706673582212209bd59957f4fa6b00e36eff4b25dad88c60c2e1f1d4da743c2991920de9a1060d64736f6c63430007010033";

    public static final String FUNC_ADDADTYPE = "addAdType";

    public static final String FUNC_ADVERTISEBOUGHT = "advertiseBought";

    public static final String FUNC_ADVERTISETYPE = "advertiseType";

    public static final String FUNC_AVAILABLEADPLANSID = "availableAdPlansId";

    public static final String FUNC_BUYADVERTISEMENT = "buyAdvertisement";

    public static final String FUNC_CONTRACTOWNER = "contractOwner";

    public static final String FUNC_GETBALANCE = "getBalance";

    public static final String FUNC_WITHDRAWALL = "withdrawAll";

    public static final Event BUYADVERTISEMENTLOG_EVENT = new Event("BuyAdvertisementLog",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
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

    public List<BuyAdvertisementLogEventResponse> getBuyAdvertisementLogEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(BUYADVERTISEMENTLOG_EVENT, transactionReceipt);
        ArrayList<BuyAdvertisementLogEventResponse> responses = new ArrayList<BuyAdvertisementLogEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BuyAdvertisementLogEventResponse typedResponse = new BuyAdvertisementLogEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.collectionId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.adType = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.promoTo = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<BuyAdvertisementLogEventResponse> buyAdvertisementLogEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, BuyAdvertisementLogEventResponse>() {
            @Override
            public BuyAdvertisementLogEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(BUYADVERTISEMENTLOG_EVENT, log);
                BuyAdvertisementLogEventResponse typedResponse = new BuyAdvertisementLogEventResponse();
                typedResponse.log = log;
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.collectionId = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.adType = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.promoTo = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<BuyAdvertisementLogEventResponse> buyAdvertisementLogEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(BUYADVERTISEMENTLOG_EVENT));
        return buyAdvertisementLogEventFlowable(filter);
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

    public static class BuyAdvertisementLogEventResponse extends BaseEventResponse {
        public String sender;

        public BigInteger collectionId;

        public BigInteger adType;

        public BigInteger promoTo;

        public BigInteger timestamp;
    }
}
