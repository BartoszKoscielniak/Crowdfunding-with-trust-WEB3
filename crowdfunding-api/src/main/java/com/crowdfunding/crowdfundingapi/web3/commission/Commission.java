package com.crowdfunding.crowdfundingapi.web3.commission;

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
import org.web3j.abi.datatypes.*;
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
import org.web3j.tuples.generated.Tuple4;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

@SuppressWarnings("rawtypes")
public class Commission extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50600180546001600160a01b031916331790556103e8600055610737806100386000396000f3fe60806040526004361061007b5760003560e01c806383a6b5651161004e57806383a6b565146101125780638d95e43014610127578063ce606ee014610147578063fa09e630146101695761007b565b806312065fe01461008057806330332a3b146100ab5780635ea1d6f8146100cd578063837a6f1d146100e2575b600080fd5b34801561008c57600080fd5b50610095610189565b6040516100a291906106f8565b60405180910390f35b3480156100b757600080fd5b506100c061018d565b6040516100a29190610641565b3480156100d957600080fd5b50610095610217565b3480156100ee57600080fd5b506101026100fd3660046104dd565b61021d565b6040516100a2949392919061061b565b61012561012036600461059a565b61025e565b005b34801561013357600080fd5b506100956101423660046104f5565b61038e565b34801561015357600080fd5b5061015c61040e565b6040516100a29190610607565b34801561017557600080fd5b506101256101843660046104b6565b61041d565b4790565b60606002805480602002602001604051908101604052809291908181526020016000905b8282101561020e576000848152602090819020604080516080810182526004860290920180546001600160a01b031683526001808201548486015260028201549284019290925260030154606083015290835290920191016101b1565b50505050905090565b60005481565b6002818154811061022a57fe5b600091825260209091206004909102018054600182015460028301546003909301546001600160a01b039092169350919084565b60408051608081018252338082526020820185815282840185815242606085018181526002805460018101825560009190915295517f405787fa12a823e0f2b7631cc41b3ba8828b3321ca811111fa75cd3aa3bb5ace600490970296870180546001600160a01b0319166001600160a01b0390921691909117905592517f405787fa12a823e0f2b7631cc41b3ba8828b3321ca811111fa75cd3aa3bb5acf86015590517f405787fa12a823e0f2b7631cc41b3ba8828b3321ca811111fa75cd3aa3bb5ad085015590517f405787fa12a823e0f2b7631cc41b3ba8828b3321ca811111fa75cd3aa3bb5ad19093019290925591517f3a1d294f0f38f0a5a86147e68f05ddf88bd717855e2a9e633af6472f6adb216e926103829290918691869161061b565b60405180910390a15050565b60405160009081906103a49084906020016105bb565b604051602081830303815290604052805190602001206040516020016103c9906105f4565b6040516020818303038152906040528051906020012014156103ec579050610408565b60005484111561040557600054848161040157fe5b0490505b90505b92915050565b6001546001600160a01b031681565b6001546001600160a01b031633146104505760405162461bcd60e51b8152600401610447906106ae565b60405180910390fd5b6001546001600160a01b0382811691161461047d5760405162461bcd60e51b8152600401610447906106ae565b6040516001600160a01b038216904780156108fc02916000818181858888f193505050501580156104b2573d6000803e3d6000fd5b5050565b6000602082840312156104c7578081fd5b81356001600160a01b0381168114610405578182fd5b6000602082840312156104ee578081fd5b5035919050565b60008060408385031215610507578081fd5b8235915060208084013567ffffffffffffffff80821115610526578384fd5b818601915086601f830112610539578384fd5b813581811115610547578485fd5b604051601f8201601f1916810185018381118282101715610566578687fd5b604052818152838201850189101561057c578586fd5b81858501868301378585838301015280955050505050509250929050565b600080604083850312156105ac578182fd5b50508035926020909101359150565b60008251815b818110156105db57602081860181015185830152016105c1565b818111156105e95782828501525b509190910192915050565b664348415249545960c81b815260070190565b6001600160a01b0391909116815260200190565b6001600160a01b0394909416845260208401929092526040830152606082015260800190565b602080825282518282018190526000919060409081850190868401855b828110156106a157815180516001600160a01b0316855286810151878601528581015186860152606090810151908501526080909301929085019060010161065e565b5091979650505050505050565b6020808252602a908201527f4f6e6c792074686520636f6e7472616374206f776e65722063616e2077697468604082015269647261772066756e647360b01b606082015260800190565b9081526020019056fea2646970667358221220ce80448f708a6fc63994ff73adf3e4e3f98b3d7fb7bde8d45aa9cae46a4bd19b64736f6c63430007010033";

    public static final String FUNC_COMMISSIONRATE = "commissionRate";

    public static final String FUNC_COMMISSIONSHISTORY = "commissionsHistory";

    public static final String FUNC_CONTRACTOWNER = "contractOwner";

    public static final String FUNC_GETBALANCE = "getBalance";

    public static final String FUNC_GETCOMMISSIONHISTORY = "getCommissionHistory";

    public static final String FUNC_GETCOMMISSIONRATE = "getCommissionRate";

    public static final String FUNC_PAYCOMMISSION = "payCommission";

    public static final String FUNC_WITHDRAWALL = "withdrawAll";

    public static final Event COMMISSIONEVENT_EVENT = new Event("CommissionEvent",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected Commission(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Commission(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Commission(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Commission(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<com.crowdfunding.crowdfundingapi.web3.commission.Commission.CommissionEventEventResponse> getCommissionEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(COMMISSIONEVENT_EVENT, transactionReceipt);
        ArrayList<com.crowdfunding.crowdfundingapi.web3.commission.Commission.CommissionEventEventResponse> responses = new ArrayList<com.crowdfunding.crowdfundingapi.web3.commission.Commission.CommissionEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            com.crowdfunding.crowdfundingapi.web3.commission.Commission.CommissionEventEventResponse typedResponse = new com.crowdfunding.crowdfundingapi.web3.commission.Commission.CommissionEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.comission = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<com.crowdfunding.crowdfundingapi.web3.commission.Commission.CommissionEventEventResponse> commissionEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, com.crowdfunding.crowdfundingapi.web3.commission.Commission.CommissionEventEventResponse>() {
            @Override
            public com.crowdfunding.crowdfundingapi.web3.commission.Commission.CommissionEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(COMMISSIONEVENT_EVENT, log);
                com.crowdfunding.crowdfundingapi.web3.commission.Commission.CommissionEventEventResponse typedResponse = new com.crowdfunding.crowdfundingapi.web3.commission.Commission.CommissionEventEventResponse();
                typedResponse.log = log;
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.comission = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<com.crowdfunding.crowdfundingapi.web3.commission.Commission.CommissionEventEventResponse> commissionEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(COMMISSIONEVENT_EVENT));
        return commissionEventEventFlowable(filter);
    }

    public RemoteFunctionCall<BigInteger> commissionRate() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_COMMISSIONRATE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple4<String, BigInteger, BigInteger, BigInteger>> commissionsHistory(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_COMMISSIONSHISTORY,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple4<String, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple4<String, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple4<String, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple4<String, BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (BigInteger) results.get(3).getValue());
                    }
                });
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

    public RemoteFunctionCall<List> getCommissionHistory() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETCOMMISSIONHISTORY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<DynamicArray<com.crowdfunding.crowdfundingapi.web3.commission.Commission.CommissionStruct>>() {}));
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

    public RemoteFunctionCall<BigInteger> getCommissionRate(BigInteger _amount, String _collectionType) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_GETCOMMISSIONRATE,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount),
                        new org.web3j.abi.datatypes.Utf8String(_collectionType)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<TransactionReceipt> payCommission(BigInteger _amount, BigInteger _commission) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_PAYCOMMISSION,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_amount),
                        new org.web3j.abi.datatypes.generated.Uint256(_commission)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> withdrawAll(String _to) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_WITHDRAWALL,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _to)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    @Deprecated
    public static com.crowdfunding.crowdfundingapi.web3.commission.Commission load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new com.crowdfunding.crowdfundingapi.web3.commission.Commission(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static com.crowdfunding.crowdfundingapi.web3.commission.Commission load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new com.crowdfunding.crowdfundingapi.web3.commission.Commission(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static com.crowdfunding.crowdfundingapi.web3.commission.Commission load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new com.crowdfunding.crowdfundingapi.web3.commission.Commission(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static com.crowdfunding.crowdfundingapi.web3.commission.Commission load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new com.crowdfunding.crowdfundingapi.web3.commission.Commission(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<com.crowdfunding.crowdfundingapi.web3.commission.Commission> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(com.crowdfunding.crowdfundingapi.web3.commission.Commission.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<com.crowdfunding.crowdfundingapi.web3.commission.Commission> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(com.crowdfunding.crowdfundingapi.web3.commission.Commission.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<com.crowdfunding.crowdfundingapi.web3.commission.Commission> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(com.crowdfunding.crowdfundingapi.web3.commission.Commission.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<com.crowdfunding.crowdfundingapi.web3.commission.Commission> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(com.crowdfunding.crowdfundingapi.web3.commission.Commission.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class CommissionStruct extends StaticStruct {
        public String sender;

        public BigInteger amount;

        public BigInteger commission;

        public BigInteger timestamp;

        public CommissionStruct(String sender, BigInteger amount, BigInteger commission, BigInteger timestamp) {
            super(new org.web3j.abi.datatypes.Address(sender),new org.web3j.abi.datatypes.generated.Uint256(amount),new org.web3j.abi.datatypes.generated.Uint256(commission),new org.web3j.abi.datatypes.generated.Uint256(timestamp));
            this.sender = sender;
            this.amount = amount;
            this.commission = commission;
            this.timestamp = timestamp;
        }

        public CommissionStruct(Address sender, Uint256 amount, Uint256 commission, Uint256 timestamp) {
            super(sender,amount,commission,timestamp);
            this.sender = sender.getValue();
            this.amount = amount.getValue();
            this.commission = commission.getValue();
            this.timestamp = timestamp.getValue();
        }
    }

    public static class CommissionEventEventResponse extends BaseEventResponse {
        public String sender;

        public BigInteger _amount;

        public BigInteger comission;

        public BigInteger timestamp;
    }
}
