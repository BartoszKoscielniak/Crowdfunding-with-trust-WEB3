package com.crowdfunding.crowdfundingapi.web3;

import com.crowdfunding.crowdfundingapi.config.PreparedResponse;
import com.crowdfunding.crowdfundingapi.config.security.AdvancedEncryptionStandard;
import com.crowdfunding.crowdfundingapi.user.User;
import com.crowdfunding.crowdfundingapi.user.UserRepository;
import com.crowdfunding.crowdfundingapi.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class Web3Service {

    private final Web3Repository repository;
    private final UserService userService;
    private final Web3j web3j = new Web3().getWeb3j();
    private final UserRepository userRepository;

    @Autowired
    private Environment env;

    public Credentials getUserCredentials() throws Exception {
        User authUser = userService.getUserFromAuthentication();
        String key = AdvancedEncryptionStandard.decrypt(authUser.getPrivateKey());
        return Credentials.create(key);
    }

    public Credentials getCrowdfundingCredentials() throws Exception {
        User crowdfunding = userRepository.findUserByPublicAddress(env.getProperty("crowdfunding.pub.address")).get();
        String key = AdvancedEncryptionStandard.decrypt(crowdfunding.getPrivateKey());
        return Credentials.create(key);
    }

    public void setContractAddress(String contractName, String contractAddress){
        Optional<Web3> contract = repository.findContractByName(contractName);
        if (contract.isEmpty()){
            Web3 newContract = new Web3(contractName, contractAddress);
            repository.save(newContract);
        } else {
            contract.get().setContractAddress(contractAddress);
            repository.save(contract.get());
        }
    }

    public ResponseEntity<Map<String, String>> sendPayableFunction(Function function, String contractAddress, BigInteger value) throws Exception {
        EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
        BigInteger baseFeePerGas = Numeric.toBigInt(block.getBaseFeePerGas());
        BigInteger maxPriorityFeePerGas = new BigInteger("2000000000");
        if (baseFeePerGas.divide(BigInteger.valueOf(10)).compareTo(maxPriorityFeePerGas) > 0){
            maxPriorityFeePerGas = baseFeePerGas.divide(BigInteger.valueOf(10));
        }
        BigInteger maxFeePerGas = baseFeePerGas.multiply(BigInteger.valueOf(3)).add(maxPriorityFeePerGas);

        String data = FunctionEncoder.encode(function);
        User authUser = userService.getUserFromAuthentication();

        Transaction tx = Transaction.createFunctionCallTransaction(authUser.getPublicAddress(), web3j.ethGetTransactionCount(authUser.getPublicAddress(),
                        DefaultBlockParameterName.LATEST).send().getTransactionCount().add(BigInteger.ONE), maxFeePerGas, block.getGasLimit(), contractAddress, data);
        EthEstimateGas ethEstimateGas = web3j.ethEstimateGas(tx).send();

        if (ethEstimateGas.hasError()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse(String.valueOf(ethEstimateGas.getError().getMessage())));
        }
        RawTransaction rawTransaction = RawTransaction.createTransaction(
                Long.parseLong(env.getProperty("crowdfunding.chain.id")),
                web3j.ethGetTransactionCount(authUser.getPublicAddress(),
                DefaultBlockParameterName.LATEST).send().getTransactionCount(),
                ethEstimateGas.getAmountUsed(),
                contractAddress,
                value,
                data,
                maxPriorityFeePerGas,
                maxFeePerGas
        );

        EthSendTransaction transactionResponse = signTransaction(rawTransaction, getUserCredentials());

        if (transactionResponse.hasError()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse(String.valueOf(transactionResponse.getError().getMessage())));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(transactionResponse.getResult()));
    }

    public ResponseEntity<Map<String, String>> sendEthTransaction(String to, Credentials senderCredentials, BigInteger value) throws IOException {
        EthBlock.Block block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock();
        BigInteger baseFeePerGas = Numeric.toBigInt(block.getBaseFeePerGas());
        BigInteger maxPriorityFeePerGas = new BigInteger("2000000000");
        if (baseFeePerGas.divide(BigInteger.valueOf(10)).compareTo(maxPriorityFeePerGas) > 0){
            maxPriorityFeePerGas = baseFeePerGas.divide(BigInteger.valueOf(10));
        }
        BigInteger maxFeePerGas = (baseFeePerGas).multiply(BigInteger.valueOf(3)).add(maxPriorityFeePerGas);

        RawTransaction transaction = RawTransaction.createEtherTransaction(
                Long.parseLong(env.getProperty("crowdfunding.chain.id")),
                web3j.ethGetTransactionCount(senderCredentials.getAddress(), DefaultBlockParameterName.LATEST).send().getTransactionCount(),
                block.getGasLimit(),
                to,
                value,
                maxPriorityFeePerGas,
                maxFeePerGas
        );

        EthSendTransaction transactionResponse = signTransaction(transaction, senderCredentials);
        if (transactionResponse.hasError()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PreparedResponse().getFailureResponse(String.valueOf(transactionResponse.getError().getMessage())));
        }
        return ResponseEntity.status(HttpStatus.OK).body(new PreparedResponse().getSuccessResponse(transactionResponse.getResult()));
    }

    private EthSendTransaction signTransaction(RawTransaction transaction, Credentials credentials) throws IOException {
        byte[] signedMessage = TransactionEncoder.signMessage(transaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        return web3j.ethSendRawTransaction(hexValue).send();
    }
}