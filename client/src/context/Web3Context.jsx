import React, {useEffect, useState, useContext} from "react";
import { AccessContext } from "./AccessContext";

export const Web3Context = React.createContext();

export const Web3Provider = ({children}) => {
    
    const URL = 'http://localhost:8080';
    const [Web3Error, setWeb3ErrorState]                                            = useState(null);
    const [Web3Success, setWeb3SuccessState]                                        = useState(null);

    const [transactionData, setTransactionData]                                     = useState(null);
    const [transactionsQuantity, setTransactionsQuantity]                           = useState(0);
    const [biggestDeposit, setBiggestDeposit]                                       = useState(0.0);
    const [totalSpend, setTotalSpend]                                               = useState(0.0);

    const [commissionTransactions, setCommissionTransactions]                       = useState(null);
    const [commissionTransactinosQuantity, setCommissionTransactinosQuantity]       = useState(0);
    const [totalCommissionSpend, setTotalCommissionSpend]                           = useState(0.0);
    const [commissionContractBalance, setCommissionContractBalance]                 = useState(null);
    const [advertiseContractBalance, setAdvertiseContractBalance]                   = useState(null);
    const { setAuthenticated, setAccessError }                                      = useContext(AccessContext);
    const [adTypes, setAdTypes]                                                     = useState(null);
    const [adTransactions, setAdTransactions]                                       = useState(null);
    const [totalAdSpend, setTotalAdSpend]                                           = useState(0);
    const [adTransactionsQuantity, setAdTransactionsQuantity]                       = useState(0);

    const setWeb3Error = (msg) => {
        setWeb3SuccessState(null)
        setWeb3ErrorState(msg)
    }

    const setWeb3Success = (msg) => {
        setWeb3SuccessState(msg)
        setWeb3ErrorState(null)
    }

    const GetFundsTransactions = async (authToken) => {
        performRequest(
            authToken,
            `/api/web3/fund/transactionhistory`,
            "GET"
        )
        .then(data => {
            if(data !== undefined){
                let totalTemp = 0.0;
                let biggestTemp = 0.0;
                Object.keys(data).map((key) => {
                    data[key]['date'] = data[key]['date'] .replace("T"," ");
                    
                    totalTemp = totalTemp + parseFloat(data[key]['amount'])
                    if(biggestTemp < parseFloat(data[key]['amount'])){
                        biggestTemp = parseFloat(data[key]['amount'])
                    }
                })
                setTransactionsQuantity(data.length)
                setTransactionData(data);
                setBiggestDeposit(biggestTemp)
                setTotalSpend(totalTemp)
            }
        })
    }

    const GetCommissionTransactions = async (authToken) => {
        CommissionContractBalance(authToken)
        performRequest(
            authToken,
            `/api/web3/commission/history`,
            "GET"
        )
        .then(data => {
            if(data !== undefined){
                let totalTemp = 0.0;
                Object.keys(data).map((key) => {
                    data[key]['date'] = data[key]['date'] .replace("T"," ");
                    
                    totalTemp = totalTemp + parseFloat(data[key]['commissinon'])
                })
                setCommissionTransactinosQuantity(data.length)
                setCommissionTransactions(data);
                setTotalCommissionSpend(totalTemp)
            }
        })
    }

    const CommissionContractBalance = async (authToken) => {
        performRequest(
            authToken,
            `/api/web3/commission/balance`,
            "GET"
        )
        .then(data => {
            if(data !== undefined){
                if (data['error'] !== undefined) {
                    setWeb3Error(data['error'])
                } else {
                    setCommissionContractBalance(parseFloat(data['result']))
                }
            }
        })
    }

    const AdvertiseContractBalance = async (authToken) => {
        performRequest(
            authToken,
            `/api/web3/ad/balance`,
            "GET"
        )
        .then(data => {
            if(data !== undefined){
                if (data['error'] !== undefined) {
                    setWeb3Error(data['error'])
                } else {
                    setAdvertiseContractBalance(parseFloat(data['result']))
                }
            }
        })
    }

    const SendFundsToOwner = async (authToken, phaseId) => {
        performRequest(
            authToken,
            `/api/web3/fund/sendfundstoowner?phaseId=${phaseId}`,
            "POST"
        )
        .then(data => {
            if(data !== undefined){
                if (data['error'] !== undefined) {
                    setWeb3Error(data['error'])
                } else {
                    setWeb3Success(<div><p className='inline-block'>Success! Check Your transaction here: </p> <a className='underline-offset-1 inline-block underline' href={`https://goerli.etherscan.io/tx/${data['result']}`} target='_blank' rel='noopener noreferrer'>etherscan.io</a></div>)
                }
            }
        })
    }

    const WithdrawCommission = async (authToken) => {
        performRequest(
            authToken,
            `/api/web3/commission/withdraw`,
            "POST"
        )
        .then(data => {
            if(data !== undefined){
                if (data['error'] !== undefined) {
                    setWeb3Error(data['error'])
                } else {
                    setWeb3Success(<div><p className='inline-block'>Success! Check Your transaction here: </p> <a className='underline-offset-1 inline-block underline' href={`https://goerli.etherscan.io/tx/${data['result']}`} target='_blank' rel='noopener noreferrer'>etherscan.io</a></div>)
                    CommissionContractBalance(authToken)
                }
            }
        })
    }

    const WithdrawAdvertiseFunds = async (authToken) => {
        performRequest(
            authToken,
            `/api/web3/ad/withdraw`,
            "POST"
        )
        .then(data => {
            if(data !== undefined){
                if (data['error'] !== undefined) {
                    setWeb3Error(data['error'])
                } else {
                    setWeb3Success(<div><p className='inline-block'>Success! Check Your transaction here: </p> <a className='underline-offset-1 inline-block underline' href={`https://goerli.etherscan.io/tx/${data['result']}`} target='_blank' rel='noopener noreferrer'>etherscan.io</a></div>)
                    AdvertiseContractBalance(authToken)
                }
            }
        })
    }

    const SendFundsToDonators = async (authToken, phaseId) => {
        performRequest(
            authToken,
            `/api/web3/fund/sendfundstodonators?phaseId=${phaseId}`,
            "POST"
        )
        .then(data => {
            if(data !== undefined){
                if (data['error'] !== undefined) {
                    setWeb3Error(data['error'])
                } else {
                    setWeb3Success(<div><p className='inline-block'>Success! Check Your transaction here: </p> <a className='underline-offset-1 inline-block underline' href={`https://goerli.etherscan.io/tx/${data['result']}`} target='_blank' rel='noopener noreferrer'>etherscan.io</a></div>)
                }
            }
        })
    }

    const BuyAdvertise = async (authToken, collectionId, advertiseId) => {
         performRequest(
            authToken,
            `/api/web3/ad/buy?collectionId=${collectionId}&advertiseId=${advertiseId}`,
            "POST"
        )
        .then(data => {
            if(data !== undefined){
                if (data['error'] !== undefined) {
                    setWeb3Error(data['error'])
                } else {
                    setWeb3Success(<div><p className='inline-block'>Success! Check Your transaction here: </p> <a className='underline-offset-1 inline-block underline' href={`https://goerli.etherscan.io/tx/${data['result']}`} target='_blank' rel='noopener noreferrer'>etherscan.io</a> <p className='inline-block'>Refresh page to see result.</p></div>)
                }
            }
        })
    }

    const GetAdvertiseTypes = async (authToken) => {
        performRequest(
            authToken,
            `/api/web3/ad/types`,
            "GET"
        )
        .then(data => {
            if(data !== undefined){
                setAdTypes(data)
            }
        }
        )
    }

    const GetAdvertiseHistory = async (authToken) => {
        AdvertiseContractBalance(authToken)
        performRequest(
            authToken,
            `/api/web3/ad/history`,
            "GET"
        )
        .then(data => {
            if(data !== undefined){
                let totalTemp = 0.0;
                Object.keys(data).map((key) => {
                    data[key]['promoTo'] = data[key]['promoTo'] .replace("T"," ");
                    data[key]['timeOfTransaction'] = data[key]['timeOfTransaction'] .replace("T"," ");

                    totalTemp = totalTemp + parseFloat(data[key]['amount'])
                    })
                setAdTransactionsQuantity(data.length)
                setAdTransactions(data)
                setTotalAdSpend(totalTemp)
            }
        })
    }

    const AddAdvertiseType = async (authToken, name, price, duration) => {
        performRequest(
            authToken,
            `/api/web3/ad/add?name=${name}&price=${price}&durationInDays=${duration}`,
            "POST"
        )
        .then(data => {
            if(data !== undefined){
                if (data['error'] !== undefined) {
                    setWeb3Error(data['error'])
                } else {
                    setWeb3Success(<div><p className='inline-block'>Success! Check Your transaction here: </p> <a className='underline-offset-1 inline-block underline' href={`https://goerli.etherscan.io/tx/${data['result']}`} target='_blank' rel='noopener noreferrer'>etherscan.io</a></div>)
                    GetAdvertiseTypes(authToken)
                }
            }
        })
    }

    const performRequest = async (authToken, url, method) => {
        return await fetch(`${URL}${url}`, {
            method: method,
            headers: {
                'Content-type': 'application/json; charset=UTF-8',
                'Access-Control-Allow-Origin': "*",
                'Authorization': authToken
            },
        })
        .then(response => {
            if(response.status === 403){
                setAccessError("Access denied. Please log in again.")
                setAuthenticated(false)
            }else{
                return response.json()
            }
        })
        .catch(err => {
            setWeb3Error("Request has failed. Try again later!")
        });
    }

    useEffect(() => {

    }, []);

    return (
        <Web3Context.Provider value={{ 
            Web3Error,
            setWeb3Error,
            Web3Success,
            setWeb3Success,
            GetFundsTransactions,
            transactionData,
            setTransactionData,
            transactionsQuantity,
            totalSpend,
            biggestDeposit,
            SendFundsToOwner,
            SendFundsToDonators,
            GetCommissionTransactions,
            commissionTransactions,
            commissionTransactinosQuantity,
            totalCommissionSpend,
            commissionContractBalance,
            WithdrawCommission,
            adTypes,
            setAdTypes,
            GetAdvertiseTypes,
            AddAdvertiseType,
            advertiseContractBalance,
            WithdrawAdvertiseFunds,
            adTransactions,
            GetAdvertiseHistory,
            totalAdSpend,
            adTransactionsQuantity,
            BuyAdvertise
            }}
            >
            {children}
        </Web3Context.Provider>
    );
};