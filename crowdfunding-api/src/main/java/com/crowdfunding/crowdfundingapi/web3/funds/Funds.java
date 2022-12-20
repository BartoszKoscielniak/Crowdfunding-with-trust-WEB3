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
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.StaticStruct;
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
import org.web3j.tuples.generated.Tuple5;
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
public class Funds extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b50600080546001600160a01b03191633179055611389806100326000396000f3fe6080604052600436106100c25760003560e01c8063243e7a871161007f578063b538d7a111610059578063b538d7a11461020b578063ce606ee01461022d578063da23ff691461024f578063e35d1a861461026f576100c2565b8063243e7a871461019a57806369a78d78146101cb57806383181609146101eb576100c2565b80630bf1b9ee146100c75780630fc6b713146100e957806312065fe0146100fc5780631f52259514610127578063208458bf1461013c578063234fad6014610169575b600080fd5b3480156100d357600080fd5b506100e76100e2366004610e8c565b61028f565b005b6100e76100f7366004610e58565b610317565b34801561010857600080fd5b5061011161054d565b60405161011e919061130b565b60405180910390f35b34801561013357600080fd5b50610111610551565b34801561014857600080fd5b5061015c610157366004610e8c565b610557565b60405161011e9190610fa6565b34801561017557600080fd5b50610189610184366004610e8c565b610570565b60405161011e959493929190610ef6565b3480156101a657600080fd5b506101ba6101b5366004610e8c565b6105b0565b60405161011e959493929190610ec5565b3480156101d757600080fd5b506100e76101e6366004610d97565b6105fa565b3480156101f757600080fd5b506100e7610206366004610e8c565b6108c8565b34801561021757600080fd5b5061022061097d565b60405161011e9190610f26565b34801561023957600080fd5b50610242610a16565b60405161011e9190610eb1565b34801561025b57600080fd5b5061015c61026a366004610e8c565b610a25565b34801561027b57600080fd5b506100e761028a366004610d6c565b610a43565b6000818152600260205260409020546001600160a01b03166102cc5760405162461bcd60e51b81526004016102c3906111a8565b60405180910390fd5b6000546001600160a01b031633146102f65760405162461bcd60e51b81526004016102c390611297565b600090815260026020819052604090912001805461ff001916610100179055565b336001600160a01b03841614156103405760405162461bcd60e51b81526004016102c3906110c4565b600082116103605760405162461bcd60e51b81526004016102c3906112e1565b6000818152600260205260409020546001600160a01b03166103c557600081815260026020819052604090912080546001600160a01b0319166001600160a01b03861617815560018101849055908101805461ffff19169055426003909101556103dd565b60008181526002602052604090206001018054830190555b6040805160a08101825233808252306020830181815283850186815260608501888152426080870181815260038054600181018255600091909152975160059098027fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85b810180546001600160a01b039a8b166001600160a01b03199182161790915595517fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85c8201805491909a1696169590951790975591517fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85d840155517fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85e83015593517fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85f9091015592517f72c6da55414e2218fda6eb36e56c04bab85d939eaa22c8d67692a831b00f2a999361053893909187918791610ec5565b60405180910390a15050600180548101905550565b4790565b60015481565b6000908152600260208190526040909120015460ff1690565b600260208190526000918252604090912080546001820154928201546003909201546001600160a01b03909116929160ff80821692610100909204169085565b600381815481106105bd57fe5b6000918252602090912060059091020180546001820154600283015460038401546004909401546001600160a01b03938416955091909216929085565b6000828152600260205260409020546001600160a01b031661062e5760405162461bcd60e51b81526004016102c3906111a8565b6000828152600260208190526040909120015460ff61010090910416151560011461066b5760405162461bcd60e51b81526004016102c390610ffd565b6000828152600260208190526040909120015460ff1615156001146106a25760405162461bcd60e51b81526004016102c390611203565b6000805b82518110156108155760008382815181106106bd57fe5b60200260200101519050600381815481106106d457fe5b906000526020600020906005020160030154600014156106f4575061080d565b6003818154811061070157fe5b60009182526020909120600590910201546001600160a01b031633146107395760405162461bcd60e51b81526004016102c390611027565b856001600160a01b03166003828154811061075057fe5b60009182526020909120600590910201546001600160a01b0316146107875760405162461bcd60e51b81526004016102c39061110e565b846003828154811061079557fe5b906000526020600020906005020160020154146107c45760405162461bcd60e51b81526004016102c390611171565b600381815481106107d157fe5b906000526020600020906005020160030154830192506000600382815481106107f657fe5b906000526020600020906005020160030181905550505b6001016106a6565b50806108335760405162461bcd60e51b81526004016102c390611260565b7f72c6da55414e2218fda6eb36e56c04bab85d939eaa22c8d67692a831b00f2a99308583864260405161086a959493929190610ec5565b60405180910390a16040516001600160a01b0385169082156108fc029083906000818181858888f193505050501580156108a8573d6000803e3d6000fd5b506000928352600260205260409092206001018054929092039091555050565b6000818152600260205260409020546001600160a01b03166108fc5760405162461bcd60e51b81526004016102c3906111a8565b6000546001600160a01b031633146109265760405162461bcd60e51b81526004016102c390611297565b60008181526002602081905260409091200154610100900460ff161561095e5760405162461bcd60e51b81526004016102c3906111d7565b600090815260026020819052604090912001805460ff19166001179055565b60606003805480602002602001604051908101604052809291908181526020016000905b82821015610a0d5760008481526020908190206040805160a0810182526005860290920180546001600160a01b039081168452600180830154909116848601526002820154928401929092526003810154606084015260040154608083015290835290920191016109a1565b50505050905090565b6000546001600160a01b031681565b60009081526002602081905260409091200154610100900460ff1690565b6000818152600260205260409020546001600160a01b0316610a775760405162461bcd60e51b81526004016102c3906111a8565b6000818152600260205260409020546001600160a01b03163314610aad5760405162461bcd60e51b81526004016102c390610fb1565b6000818152600260205260409020546001600160a01b03838116911614610ae65760405162461bcd60e51b81526004016102c390610fb1565b600081815260026020526040902060010154610b145760405162461bcd60e51b81526004016102c390611145565b6000818152600260208190526040909120015460ff610100909104161515600114610b515760405162461bcd60e51b81526004016102c390610ffd565b6000818152600260208190526040909120015460ff1615610b845760405162461bcd60e51b81526004016102c39061106e565b60008181526002602052604090819020805460019091015491517f72c6da55414e2218fda6eb36e56c04bab85d939eaa22c8d67692a831b00f2a9992610bdc9230926001600160a01b03909116919086904290610ec5565b60405180910390a16040805160a08101825230815260008381526002602081815284832080546001600160a01b039081168387019081528688018981528585526001938401805460608a019081524260808b019081526003805497880181558a52995160059096027fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85b810180549787166001600160a01b031998891617905593517fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85c85018054918716919097161790955590517fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85d83015592517fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85e82015595517fc2575a0e9e593c00f959f8c92f12db2869c3395a3b0502d05e2516446f71f85f909601959095558684529190525492519185169280156108fc02929091818181858888f19350505050158015610d56573d6000803e3d6000fd5b5060009081526002602052604081206001015550565b60008060408385031215610d7e578182fd5b8235610d898161133b565b946020939093013593505050565b600080600060608486031215610dab578081fd5b8335610db68161133b565b92506020848101359250604085013567ffffffffffffffff80821115610dda578384fd5b818701915087601f830112610ded578384fd5b813581811115610dfb578485fd5b8381029150610e0b848301611314565b8181528481019084860184860187018c1015610e25578788fd5b8795505b83861015610e47578035835260019590950194918601918601610e29565b508096505050505050509250925092565b600080600060608486031215610e6c578283fd5b8335610e778161133b565b95602085013595506040909401359392505050565b600060208284031215610e9d578081fd5b5035919050565b6001600160a01b03169052565b6001600160a01b0391909116815260200190565b6001600160a01b03958616815293909416602084015260408301919091526060820152608081019190915260a00190565b6001600160a01b039590951685526020850193909352901515604084015215156060830152608082015260a00190565b602080825282518282018190526000919060409081850190868401855b82811015610f9957815180516001600160a01b0316855286810151610f6a88870182610ea4565b508086015185870152606080820151908601526080908101519085015260a09093019290850190600101610f43565b5091979650505050505050565b901515815260200190565b6020808252602c908201527f4f6e6c792074686520636f6c6c656374696f6e206f776e65722063616e20776960408201526b7468647261772066756e647360a01b606082015260800190565b60208082526010908201526f141bdb1b081a185cc81b9bdd08195b9960821b604082015260600190565b60208082526027908201527f4f6e6c79207468652066756e6473206f776e65722063616e2077697468647261604082015266772066756e647360c81b606082015260800190565b60208082526036908201527f436f6c6c656374696f6e207374617475732069732066726175642e20596f752060408201527563616e6e6f742077697468647261772066756e64732160501b606082015260800190565b6020808252602a908201527f596f752063616e6e6f74206465706f7369742066756e647320746f206f776e2060408201526931b7b63632b1ba34b7b760b11b606082015260800190565b60208082526018908201527f496e76616c696420726563656976657220616464726573730000000000000000604082015260600190565b602080825260129082015271496e73756666696369656e742066756e647360701b604082015260600190565b60208082526017908201527f496e636f727265637420436f6c6c656374696f6e204944000000000000000000604082015260600190565b602080825260159082015274125b9d985b1a590818dbdb1b1958dd1a5bdb881251605a1b604082015260600190565b602080825260129082015271141bdb1b08185b1c9958591e48195b99195960721b604082015260600190565b60208082526039908201527f436f6c6c656374696f6e20737461747573206973206e6f742066726175642e2060408201527f596f752063616e6e6f742077697468647261772066756e647300000000000000606082015260800190565b60208082526017908201527f4e6f2066756e647320746f20626520726566756e646564000000000000000000604082015260600190565b6020808252602a908201527f4f6e6c7920636f6e7472616374206f776e65722063616e2073657420737461746040820152691d5cc81bd9881c1bdb1b60b21b606082015260800190565b60208082526010908201526f125b98dbdc9c9958dd08185b5bdd5b9d60821b604082015260600190565b90815260200190565b60405181810167ffffffffffffffff8111828210171561133357600080fd5b604052919050565b6001600160a01b038116811461135057600080fd5b5056fea2646970667358221220c97602e3036a50d8062454ae5f949eb080a871ca8cb545fdf93dc9d9779caa4c64736f6c63430007010033";

    public static final String FUNC_CONTRACTOWNER = "contractOwner";

    public static final String FUNC_DEPOSITFUNDS = "depositFunds";

    public static final String FUNC_DONATIONSCOUNT = "donationsCount";

    public static final String FUNC_FUNDSDONATED = "fundsDonated";

    public static final String FUNC_GETBALANCE = "getBalance";

    public static final String FUNC_GETTRANSACTIONHIOSTORY = "getTransactionHiostory";

    public static final String FUNC_ISFRAUD = "isFraud";

    public static final String FUNC_ISPOLLENDED = "isPollEnded";

    public static final String FUNC_SENDFUNDSTODONATORS = "sendFundsToDonators";

    public static final String FUNC_SENDFUNDSTOOWNER = "sendFundsToOwner";

    public static final String FUNC_SETFRAUD = "setFraud";

    public static final String FUNC_SETPOLLENDED = "setPollEnded";

    public static final String FUNC_TRANSACTIONHISTORY = "transactionHistory";

    public static final Event FUNDSEMITION_EVENT = new Event("FundsEmition",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
    ;

    @Deprecated
    protected Funds(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Funds(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Funds(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Funds(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<FundsEmitionEventResponse> getFundsEmitionEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(FUNDSEMITION_EVENT, transactionReceipt);
        ArrayList<FundsEmitionEventResponse> responses = new ArrayList<FundsEmitionEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            FundsEmitionEventResponse typedResponse = new FundsEmitionEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.receiver = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse._collectionId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<FundsEmitionEventResponse> fundsEmitionEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, FundsEmitionEventResponse>() {
            @Override
            public FundsEmitionEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(FUNDSEMITION_EVENT, log);
                FundsEmitionEventResponse typedResponse = new FundsEmitionEventResponse();
                typedResponse.log = log;
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.receiver = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse._collectionId = (BigInteger) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.timestamp = (BigInteger) eventValues.getNonIndexedValues().get(4).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<FundsEmitionEventResponse> fundsEmitionEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(FUNDSEMITION_EVENT));
        return fundsEmitionEventFlowable(filter);
    }

    public RemoteFunctionCall<String> contractOwner() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_CONTRACTOWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteFunctionCall<TransactionReceipt> depositFunds(String _receiver, BigInteger _amount, BigInteger _collectionId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_DEPOSITFUNDS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _receiver),
                        new org.web3j.abi.datatypes.generated.Uint256(_amount),
                        new org.web3j.abi.datatypes.generated.Uint256(_collectionId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<BigInteger> donationsCount() {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_DONATIONSCOUNT,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteFunctionCall<Tuple5<String, BigInteger, Boolean, Boolean, BigInteger>> fundsDonated(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_FUNDSDONATED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Bool>() {}, new TypeReference<Bool>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple5<String, BigInteger, Boolean, Boolean, BigInteger>>(function,
                new Callable<Tuple5<String, BigInteger, Boolean, Boolean, BigInteger>>() {
                    @Override
                    public Tuple5<String, BigInteger, Boolean, Boolean, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, BigInteger, Boolean, Boolean, BigInteger>(
                                (String) results.get(0).getValue(),
                                (BigInteger) results.get(1).getValue(),
                                (Boolean) results.get(2).getValue(),
                                (Boolean) results.get(3).getValue(),
                                (BigInteger) results.get(4).getValue());
                    }
                });
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

    public RemoteFunctionCall<Boolean> isFraud(BigInteger _collectionId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISFRAUD,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_collectionId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<Boolean> isPollEnded(BigInteger _collectionId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_ISPOLLENDED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_collectionId)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteFunctionCall<TransactionReceipt> sendFundsToDonators(String _toReceive, BigInteger _collectionId, List<BigInteger> _transactionArray) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SENDFUNDSTODONATORS,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _toReceive),
                        new org.web3j.abi.datatypes.generated.Uint256(_collectionId),
                        new org.web3j.abi.datatypes.DynamicArray<org.web3j.abi.datatypes.generated.Uint256>(
                                org.web3j.abi.datatypes.generated.Uint256.class,
                                org.web3j.abi.Utils.typeMap(_transactionArray, org.web3j.abi.datatypes.generated.Uint256.class))),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> sendFundsToOwner(String _toReceive, BigInteger _collectionId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SENDFUNDSTOOWNER,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.Address(160, _toReceive),
                        new org.web3j.abi.datatypes.generated.Uint256(_collectionId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setFraud(BigInteger _collectionId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETFRAUD,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_collectionId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<TransactionReceipt> setPollEnded(BigInteger _collectionId) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(
                FUNC_SETPOLLENDED,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(_collectionId)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteFunctionCall<Tuple5<String, String, BigInteger, BigInteger, BigInteger>> transactionHistory(BigInteger param0) {
        final org.web3j.abi.datatypes.Function function = new org.web3j.abi.datatypes.Function(FUNC_TRANSACTIONHISTORY,
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        return new RemoteFunctionCall<Tuple5<String, String, BigInteger, BigInteger, BigInteger>>(function,
                new Callable<Tuple5<String, String, BigInteger, BigInteger, BigInteger>>() {
                    @Override
                    public Tuple5<String, String, BigInteger, BigInteger, BigInteger> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple5<String, String, BigInteger, BigInteger, BigInteger>(
                                (String) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (BigInteger) results.get(2).getValue(),
                                (BigInteger) results.get(3).getValue(),
                                (BigInteger) results.get(4).getValue());
                    }
                });
    }

    @Deprecated
    public static Funds load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Funds(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Funds load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Funds(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Funds load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Funds(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Funds load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Funds(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Funds> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Funds.class, web3j, credentials, contractGasProvider, BINARY, "");
    }

    public static RemoteCall<Funds> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return deployRemoteCall(Funds.class, web3j, transactionManager, contractGasProvider, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Funds> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Funds.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    @Deprecated
    public static RemoteCall<Funds> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(Funds.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static class TransactionStruct extends StaticStruct {
        public String sender;

        public String receiver;

        public BigInteger _collectionId;

        public BigInteger amount;

        public BigInteger timestamp;

        public TransactionStruct(String sender, String receiver, BigInteger _collectionId, BigInteger amount, BigInteger timestamp) {
            super(new org.web3j.abi.datatypes.Address(sender),new org.web3j.abi.datatypes.Address(receiver),new org.web3j.abi.datatypes.generated.Uint256(_collectionId),new org.web3j.abi.datatypes.generated.Uint256(amount),new org.web3j.abi.datatypes.generated.Uint256(timestamp));
            this.sender = sender;
            this.receiver = receiver;
            this._collectionId = _collectionId;
            this.amount = amount;
            this.timestamp = timestamp;
        }

        public TransactionStruct(Address sender, Address receiver, Uint256 _collectionId, Uint256 amount, Uint256 timestamp) {
            super(sender,receiver,_collectionId,amount,timestamp);
            this.sender = sender.getValue();
            this.receiver = receiver.getValue();
            this._collectionId = _collectionId.getValue();
            this.amount = amount.getValue();
            this.timestamp = timestamp.getValue();
        }
    }

    public static class FundsEmitionEventResponse extends BaseEventResponse {
        public String sender;

        public String receiver;

        public BigInteger amount;

        public BigInteger _collectionId;

        public BigInteger timestamp;
    }
}
