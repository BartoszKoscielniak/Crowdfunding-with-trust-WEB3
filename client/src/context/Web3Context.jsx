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

    const setWeb3Error = (msg) => {
        setWeb3SuccessState(null)
        setWeb3ErrorState(msg)
    }

    const setWeb3Success = (msg) => {
        setWeb3SuccessState(msg)
        setWeb3ErrorState(null)
    }

    const GetMyInformation = async (authToken) => {
        return await fetch(`${URL}/api/user/me`, {
            method: 'GET',
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
            .then(data => {
                setUserData(data);
            })
            .catch(err => {
                setWeb3Error("Failed to fetch collections. Try again later!")
            });
    }

    const GetFundsTransactions = async (authToken) => {
        return await fetch(`${URL}/api/web3/fund/transactionhistory`, {
            method: 'GET',
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
            .then(data => {
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
            })
            .catch(err => {
                setWeb3Error("Failed to fetch transactions. Try again later!")
            });
    }

    const GetCommissionTransactions = async (authToken) => {
        CommissionContractBalance(authToken)
        return await fetch(`${URL}/api/web3/commission/history`, {
            method: 'GET',
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
            .then(data => {
                let totalTemp = 0.0;
                Object.keys(data).map((key) => {
                    data[key]['date'] = data[key]['date'] .replace("T"," ");
                    
                    totalTemp = totalTemp + parseFloat(data[key]['commissinon'])
                })
                setCommissionTransactinosQuantity(data.length)
                setCommissionTransactions(data);
                setTotalCommissionSpend(totalTemp)
            })
            .catch(err => {
                setWeb3Error("Failed to fetch transactions. Try again later!")
            });
    }

    const CommissionContractBalance = async (authToken) => {
        return await fetch(`${URL}/api/web3/commission/balance`, {
            method: 'GET',
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
        .then(data => {
            if (data['error'] !== undefined) {
                setWeb3Error(data['error'])
            } else {
                setCommissionContractBalance(parseFloat(data['result']))
            }
        })
        .catch(err => {
            setWeb3Error(err.message)
        });
    }

    const AdvertiseContractBalance = async (authToken) => {
        return await fetch(`${URL}/api/web3/ad/balance`, {
            method: 'GET',
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
        .then(data => {
            if (data['error'] !== undefined) {
                setWeb3Error(data['error'])
            } else {
                setAdvertiseContractBalance(parseFloat(data['result']))
            }
        })
        .catch(err => {
            setWeb3Error(err.message)
        });
    }

    const SendFundsToOwner = async (authToken, phaseId) => {
        return await fetch(`${URL}/api/web3/fund/sendfundstoowner?phaseId=${phaseId}`, {
            method: 'POST',
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
        .then(data => {
            if (data['error'] !== undefined) {
                setWeb3Error(data['error'])
            } else {
                setWeb3Success(data['result'])
            }
        })
        .catch(err => {
            setWeb3Error(err.message)
        });
    }

    const WithdrawCommission = async (authToken) => {
        return await fetch(`${URL}/api/web3/commission/withdraw`, {
            method: 'POST',
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
        .then(data => {
            if (data['error'] !== undefined) {
                setWeb3Error(data['error'])
            } else {
                setWeb3Success(<div><p className='inline-block'>Success! Check Your transaction here: </p> <a className='underline-offset-1 inline-block underline' href={`https://goerli.etherscan.io/tx/${data['result']}`} target='_blank' rel='noopener noreferrer'>etherscan.io</a></div>)
            }
        })
        .catch(err => {
            setWeb3Error(err.message)
        });
    }

    const WithdrawAdvertiseFunds = async (authToken) => {
        return await fetch(`${URL}/api/web3/ad/withdraw`, {
            method: 'POST',
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
        .then(data => {
            if (data['error'] !== undefined) {
                setWeb3Error(data['error'])
            } else {
                setWeb3Success(<div><p className='inline-block'>Success! Check Your transaction here: </p> <a className='underline-offset-1 inline-block underline' href={`https://goerli.etherscan.io/tx/${data['result']}`} target='_blank' rel='noopener noreferrer'>etherscan.io</a></div>)
            }
        })
        .catch(err => {
            setWeb3Error(err.message)
        });
    }

    const SendFundsToDonators = async (authToken, phaseId) => {
        return await fetch(`${URL}/api/web3/fund/sendfundstodonators?phaseId=${phaseId}`, {
            method: 'POST',
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
        .then(data => {
            if (data['error'] !== undefined) {
                setWeb3Error(data['error'])
            } else {
                setWeb3Success(data['result'])
            }
        })
        .catch(err => {
            setWeb3Error(err.message)
        });
    }

    const GetAdvertiseTypes = async (authToken) => {
        AdvertiseContractBalance(authToken)
        return await fetch(`${URL}/api/web3/ad/types`, {
            method: 'GET',
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
            .then(data => setAdTypes(data))
            .catch(err => {
                setWeb3Error("Failed to fetch transactions. Try again later!")
            });
    }

    const AddAdvertiseType = async (authToken, name, price, duration) => {
        return await fetch(`${URL}/api/web3/ad/add?name=${name}&price=${price}&durationInDays=${duration}`, {
            method: 'POST',
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
            .then(data => {
                if (data['error'] !== undefined) {
                    setWeb3Error(<div><p className='inline-block'>Error! Check for details here: </p> <a className='underline-offset-1 inline-block underline' href={`https://goerli.etherscan.io/tx/${data['error']}`} target='_blank' rel='noopener noreferrer'>etherscan.io</a></div>)
                } else {
                    setWeb3Success(data['result'])
                    GetAdvertiseTypes(authToken)
                }
            })
            .catch(err => {
                setWeb3Error(err.message)
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
            WithdrawAdvertiseFunds
            }}
            >
            {children}
        </Web3Context.Provider>
    );
};