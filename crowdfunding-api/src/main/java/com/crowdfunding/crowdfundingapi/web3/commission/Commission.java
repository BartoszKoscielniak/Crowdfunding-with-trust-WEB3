package com.crowdfunding.crowdfundingapi.web3.commission;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
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
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

@SuppressWarnings("rawtypes")
public class Commission extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50600180546001600160a01b031916331790556103e860005561056f806100386000396000f3fe6080604052600436106100555760003560e01c806312065fe01461005a5780635ea1d6f81461008557806383a6b5651461009a5780638d95e430146100af578063ce606ee0146100cf578063fa09e630146100f1575b600080fd5b34801561006657600080fd5b5061006f610111565b60405161007c9190610530565b60405180910390f35b34801561009157600080fd5b5061006f610115565b6100ad6100a836600461043f565b61011b565b005b3480156100bb57600080fd5b5061006f6100ca36600461039a565b61024b565b3480156100db57600080fd5b506100e46102cb565b60405161007c91906104ac565b3480156100fd57600080fd5b506100ad61010c366004610373565b6102da565b4790565b60005481565b60408051608081018252338082526020820185815282840185815242606085018181526002805460018101825560009190915295517f405787fa12a823e0f2b7631cc41b3ba8828b3321ca811111fa75cd3aa3bb5ace600490970296870180546001600160a01b0319166001600160a01b0390921691909117905592517f405787fa12a823e0f2b7631cc41b3ba8828b3321ca811111fa75cd3aa3bb5acf86015590517f405787fa12a823e0f2b7631cc41b3ba8828b3321ca811111fa75cd3aa3bb5ad085015590517f405787fa12a823e0f2b7631cc41b3ba8828b3321ca811111fa75cd3aa3bb5ad19093019290925591517f3a1d294f0f38f0a5a86147e68f05ddf88bd717855e2a9e633af6472f6adb216e9261023f929091869186916104c0565b60405180910390a15050565b6040516000908190610261908490602001610460565b6040516020818303038152906040528051906020012060405160200161028690610499565b6040516020818303038152906040528051906020012014156102a95790506102c5565b6000548411156102c25760005484816102be57fe5b0490505b90505b92915050565b6001546001600160a01b031681565b6001546001600160a01b0316331461030d5760405162461bcd60e51b8152600401610304906104e6565b60405180910390fd5b6001546001600160a01b0382811691161461033a5760405162461bcd60e51b8152600401610304906104e6565b6040516001600160a01b038216904780156108fc02916000818181858888f1935050505015801561036f573d6000803e3d6000fd5b5050565b600060208284031215610384578081fd5b81356001600160a01b03811681146102c2578182fd5b600080604083850312156103ac578081fd5b8235915060208084013567ffffffffffffffff808211156103cb578384fd5b818601915086601f8301126103de578384fd5b8135818111156103ec578485fd5b604051601f8201601f191681018501838111828210171561040b578687fd5b6040528181528382018501891015610421578586fd5b81858501868301378585838301015280955050505050509250929050565b60008060408385031215610451578182fd5b50508035926020909101359150565b60008251815b818110156104805760208186018101518583015201610466565b8181111561048e5782828501525b509190910192915050565b664348415249545960c81b815260070190565b6001600160a01b0391909116815260200190565b6001600160a01b0394909416845260208401929092526040830152606082015260800190565b6020808252602a908201527f4f6e6c792074686520636f6e7472616374206f776e65722063616e2077697468604082015269647261772066756e647360b01b606082015260800190565b9081526020019056fea264697066735822122042070ebf1e446c131e58a04d569f4cfa552017dd5a35344691384a655efee09e64736f6c63430007010033";

    public static final String FUNC_COMMISSIONRATE = "commissionRate";

    public static final String FUNC_CONTRACTOWNER = "contractOwner";

    public static final String FUNC_GETBALANCE = "getBalance";

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

    public List<CommissionEventEventResponse> getCommissionEventEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(COMMISSIONEVENT_EVENT, transactionReceipt);
        ArrayList<CommissionEventEventResponse> responses = new ArrayList<CommissionEventEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            CommissionEventEventResponse typedResponse = new CommissionEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.comission = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<CommissionEventEventResponse> commissionEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, CommissionEventEventResponse>() {
            @Override
            public CommissionEventEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(COMMISSIONEVENT_EVENT, log);
                CommissionEventEventResponse typedResponse = new CommissionEventEventResponse();
                typedResponse.log = log;
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._amount = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.comission = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<CommissionEventEventResponse> commissionEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
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
    public static Commission load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Commission(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Commission load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Commission(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Commission load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Commission(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Commission load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Commission(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Commission> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Commission.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Commission> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Commission.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Commission> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Commission.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Commission> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Commission.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class CommissionEventEventResponse extends BaseEventResponse {
        public String sender;

        public BigInteger _amount;

        public BigInteger comission;

        public BigInteger timestamp;
    }
}
