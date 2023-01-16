import { useContext, useEffect, useState } from "react";
import { Web3Context } from '../../context/Web3Context';
import { CollectionContext } from '../../context/CollectionContext';
import { AccessContext } from '../../context/AccessContext';
import { AccountContext } from '../../context/AccountContext';
import { AiOutlineLoading3Quarters } from "react-icons/ai";
import { ErrorAlert, SuccessAlert, Input} from '../../components'
import { Navigate } from "react-router-dom";

const CollectionsView = () => {
    const { authenticated, bearerToken, login, setAccessError } = useContext(AccessContext);
    const { accountError, setAccountError, accountSuccess, setAccountSuccess, userData, GetMyInformation, ChangePassword, ChangeDetails } = useContext(AccountContext);
    const { Web3Error, setWeb3Error, Web3Success, setWeb3Success, GetFundsTransactions, transactionData, transactionsQuantity, totalSpend,
            biggestDeposit, SendFundsToOwner, SendFundsToDonators, GetCommissionTransactions, commissionTransactions, commissionTransactinosQuantity,
            totalCommissionSpend, commissionContractBalance, WithdrawCommission, adTypes, GetAdvertiseTypes, AddAdvertiseType, advertiseContractBalance, WithdrawAdvertiseFunds,
            adTransactions, GetAdvertiseHistory, totalAdSpend, adTransactionsQuantity } = useContext(Web3Context);

    const { GetOwnedPhases, GetSupportedPhases, supportedFraudPhases, ownedSuccessPhases } = useContext(CollectionContext);
    const [loadingSpinner, setLoadingSpinner]           = useState(false)
    const [submitLoadingSpinner, setSubmitLoadingSpinner]           = useState(false)
    const [accountInfo, setAccountInfo]                 = useState(true)

    const [transactionHistory, setTransactionHistory]   = useState(false)
    const [collectionInfo, setCollectionInfo]           = useState(false)
    const [commissionInfo, setCommissionInfo]           = useState(false)
    const [statsInfo, setStatsInfo]                     = useState(false)
    const [passwordData, setPasswordData]               = useState({ oldPassword: '', newPassword: '', repeatedNewPassword: '', nameInput: '', lastNameInput: '', adNameInput: '', adPriceInput: '', adDurationInput: '' })
    const [openFundsMenu, setOpenFundsMenu]             = useState(false)
    const [openAdminMenu, setAdminMenu]                 = useState(false)
    const [advertiseHistory, setAdvertiseHistory]       = useState(false)
    const [changePasswordMode, setChangePasswordMode]   = useState(false)
    const [changeDetailsMode, setChangeDetailsMode]     = useState(false)
    const [newAdvertisePanel, setNewAdvertisePanel]     = useState(false)
    const [clickedButton, setClickedButton]             = useState(null)

    const handleChange = (e, name) => {
        setPasswordData((prevState) => ({ ...prevState, [name]: e.target.value }));
    }

    useEffect(() => {
        if (!authenticated) {
            setAccountError("You have to be logged in!")
            setAccessError("You have to be logged in!")
        } else {
            GetMyInformation(bearerToken);
        }
    }, [])

    return (
        <div>{!authenticated ? (<Navigate replace to="/" />):(
            <div className="bg-neutral-800 w-full absolute mf:flex-row md:px-20 md:py-28 px-4 h-[1000px]">
                <div className="max-h-[750px]">
                    <div className="w-1/4 foat-left inline-block p-1 text-white text-lg">
                        {userData !== null && userData !== undefined && userData['name'] !== undefined && userData['lastname'] !== null &&(
                            <p className="text-center mb-1 text-2xl">{userData['name']} {userData['lastname']}</p>
                        )}
                        <div className="border rounded-xl p-2">
                            <button
                                className="hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150 w-full "
                                onClick={() => {
                                    setAccountInfo(true);
                                    setTransactionHistory(false);
                                    setCollectionInfo(false);
                                    setCommissionInfo(false);
                                    setStatsInfo(false);
                                    setAdvertiseHistory(false);
                                }}
                            >
                                Account
                            </button>
                            <button
                                className="hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150 w-full"
                                onClick={() => {
                                    setOpenFundsMenu(!openFundsMenu)
                                }}
                            >
                                Funds 
                            </button>
                            {openFundsMenu && (
                                <div>
                                <button
                                className="bg-neutral-700 hover:bg-neutral-600 p-1 mt-1 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150 w-full"
                                onClick={() => {
                                    setAccountInfo(false);
                                    setTransactionHistory(true);
                                    setCollectionInfo(false);
                                    setCommissionInfo(false);
                                    setStatsInfo(false);
                                    setAdvertiseHistory(false);
                                    if(transactionData === null) GetFundsTransactions(bearerToken, login)
                                }}
                                >
                                Transaction History
                                </button>
                                <button
                                className="bg-neutral-700 hover:bg-neutral-600 p-1 mt-1 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150 w-full"
                                onClick={() => {
                                    setAccountInfo(false);
                                    setTransactionHistory(false);
                                    setCollectionInfo(true);
                                    setCommissionInfo(false);
                                    setStatsInfo(false);
                                    setAdvertiseHistory(false);
                                    if(supportedFraudPhases === null) GetSupportedPhases(bearerToken)
                                    if(ownedSuccessPhases === null) GetOwnedPhases(bearerToken)
                                }}
                                >
                                Collection Funds
                                </button>
                                </div>
                            )}
                            {userData !== null && userData['userRole'] === 'ADMIN' && (
                                <button
                                className="hover:bg-neutral-700 p-2 mt-1 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150 w-full"
                                onClick={() => {
                                    setAdminMenu(!openAdminMenu)
                                }}
                                >
                                Admin Tools
                                </button>
                            )}
                            {openAdminMenu && (
                                <div>
                                <button
                                className="bg-neutral-700 hover:bg-neutral-600 p-1 mt-1 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150 w-full"
                                onClick={() => {
                                    setAccountInfo(false);
                                    setTransactionHistory(false);
                                    setCollectionInfo(false);
                                    setCommissionInfo(true);
                                    setStatsInfo(false);
                                    setAdvertiseHistory(false);
                                    if(commissionTransactions === null) GetCommissionTransactions(bearerToken)
                                }}
                                >
                                Commission
                                </button>
                                <button
                                className="bg-neutral-700 hover:bg-neutral-600 p-1 mt-1 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150 w-full"
                                onClick={() => {
                                    setAccountInfo(false);
                                    setTransactionHistory(false);
                                    setCollectionInfo(false);
                                    setCommissionInfo(false)
                                    setStatsInfo(true);
                                    setAdvertiseHistory(false);
                                    if(adTypes === null) GetAdvertiseTypes(bearerToken);
                                }}
                                >
                                Add Advertise
                                </button>
                                <button
                                className="bg-neutral-700 hover:bg-neutral-600 p-1 mt-1 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150 w-full"
                                onClick={() => {
                                    setAccountInfo(false);
                                    setTransactionHistory(false);
                                    setCollectionInfo(false);
                                    setCommissionInfo(false)
                                    setStatsInfo(false);
                                    setAdvertiseHistory(true);
                                    if(adTransactions === null) GetAdvertiseHistory(bearerToken);
                                }}
                                >
                                Advertise History
                                </button>
                                </div>
                            )}
                        </div>
                    </div>
                    <div className="w-3/4 inline-block p-1 float-right">
                        <div className="border rounded-xl p-4">

                            {accountInfo && userData !== null && userData !== undefined && (
                                <div className='w-full max-h-[650px] h-[650px] text-white text-xl p-1 overflow-auto'>
                                    <h1 className="text-3xl pb-1">Contact</h1>
                                    <p className="text-xl pb-3">Contact details for Your account</p>
                                    <div className="w-3/4 border-white border rounded-lg  divide-y bg-neutral-700 hover:bg-neutral-600 p-2 hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150">
                                        <p className="py-4 ">E-mail address: {userData['email']}</p>
                                        <p className="py-4 ">Phone number: {userData['phoneNumber']}</p>
                                    </div>
                                    <h1 className="text-3xl pb-1 pt-5 block">Security</h1>
                                    <p className="text-xl pb-3">Keep Your account safe</p>
                                    <div className="w-3/4 border-white border rounded-lg bg-neutral-700 hover:bg-neutral-600 p-2 hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150">
                                        <p className="py-4 inline-block">Change password: </p>
                                        <button
                                        className="text-white inline-block text-lg p-2 mx-1 mb-1 rounded-lg bg-yellow-700 transition ease-in-out delay-50 hover:bg-yellow-500 duration-200"
                                        onClick={() => setChangePasswordMode(!changePasswordMode)}
                                        >
                                            Change
                                        </button>
                                        {changePasswordMode === true && (
                                            <div className="">
                                            <Input placeholder="Old password" name="oldPassword" type="password" handleChange={handleChange} />
                                            <Input placeholder="New password" name="newPassword" type="password" handleChange={handleChange} />
                                            <Input placeholder="Repeat new password" name="repeatedNewPassword" type="password" handleChange={handleChange} />
                                            <button
                                                onClick={() => {
                                                    if (passwordData['newPassword'].length !== 0 && passwordData['oldPassword'].length !== 0 && passwordData['repeatedNewPassword'].length !== 0) {
                                                        if (passwordData['newPassword'] === passwordData['repeatedNewPassword']) {
                                                            ChangePassword(bearerToken, passwordData['oldPassword'], passwordData['newPassword']);
                                                        } else {
                                                            setAccountError('Passwords are not same')
                                                        }
                                                    } else {
                                                        setAccountError('Fill in all fields!')
                                                    }
                                                }}
                                                className="w-full text-white inline-block text-lg p-2 mx-1 mb-1 rounded-lg bg-green-700 transition ease-in-out delay-50 hover:bg-green-500 duration-200"
                                            >
                                                Submit
                                            </button>
                                            </div>
                                        )}
                                    </div>
                                    <h1 className="text-3xl pb-1 pt-5 block">Details</h1>
                                    <p className="text-xl pb-3">Change Your private informations </p>
                                    <div className="w-3/4 border-white border rounded-lg  bg-neutral-700 hover:bg-neutral-600 p-2 hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150">
                                        <p className="py-4 inline-block">Name & Lastname: {userData['name']} {userData['lastname']}</p>
                                        <button
                                        className="text-white inline-block text-lg p-2 mx-1 mb-1 rounded-lg bg-yellow-700 transition ease-in-out delay-50 hover:bg-yellow-500 duration-200"
                                        onClick={() => setChangeDetailsMode(!changeDetailsMode)}
                                        >
                                            Change
                                        </button>
                                        {changeDetailsMode === true && (
                                            <div className="">
                                            <Input placeholder="Name" name="nameInput" type="text" handleChange={handleChange} />
                                            <Input placeholder="Lastname" name="lastNameInput" type="text" handleChange={handleChange} />
                                            <button
                                                onClick={() => {
                                                    if (passwordData['nameInput'].length !== 0 && passwordData['lastNameInput'].length !== 0) {
                                                        ChangeDetails(bearerToken, passwordData['nameInput'], passwordData['lastNameInput'])
                                                    } else {
                                                        setAccountError('Fill in all fields!')
                                                    }
                                                }}
                                                className="w-full text-white inline-block text-lg p-2 mx-1 mb-1 rounded-lg bg-green-700 transition ease-in-out delay-50 hover:bg-green-500 duration-200"
                                            >
                                                Submit
                                            </button>
                                        </div>
                                        )}
                                    </div>
                                    <h1 className="text-3xl pb-1 pt-5 block">Address</h1>
                                    <p className="text-xl pb-3">Your Ethereum address used to interact with Our service</p>
                                    <div className="w-3/4 border-white border rounded-lg  divide-y bg-neutral-700 hover:bg-neutral-600 p-2 hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150">
                                        <p className="py-4 ">Public address: <a className='underline-offset-1 inline-block underline' href={`https://goerli.etherscan.io/address/${userData['publicAddress']}`} target='_blank' rel='noopener noreferrer'>{userData['publicAddress']}</a></p>
                                    </div>
                                </div>
                            )}

                            {transactionHistory && (
                                transactionData !== null ? (
                                    <div className="relative text-white text-xl max-h-[650px] h-[650px]">
                                        <div className="overflow-auto h-[570px]">
                                            {
                                                Object.keys(transactionData).map((key) => {
                                                    return <div key={key} className="hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150">
                                                        <p>{transactionData[key]['phaseName']}</p>
                                                        <p>Amount: {transactionData[key]['amount'].toFixed(4)} ETH</p>
                                                        <p>{transactionData[key]['date']}</p>
                                                    </div>
                                                })
                                            }
                                        </div>
                                        <div className="absolute left-0 right-0 bottom-0 p-3 bg-neutral-800 w-full h-[80px]">
                                            <div className="grid gap-2 grid-cols-3 grid-rows-1 text-center">
                                                <p className="inline-block mr-3">Transactions: <br /> {transactionsQuantity}</p>
                                                <p className="inline-block mr-3">Total funds spend:<br /> {totalSpend.toFixed(4)} ETH</p>
                                                <p className="inline-block mr-3">Biggest deposit: <br /> {biggestDeposit.toFixed(4)} ETH</p>
                                            </div>
                                        </div>
                                    </div>
                                ) : (
                                    <div className="h-[650px] text-white text-xl relative">
                                        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2">
                                            <AiOutlineLoading3Quarters fontSize={65} className="text-white inline-block animate-spin" />
                                        </div>
                                    </div>
                                )
                            )}

                            {collectionInfo && (
                                supportedFraudPhases !== null && ownedSuccessPhases !== null ? (
                                    <div className="relative text-white text-xl max-h-[650px] h-[650px]">
                                        <div className="overflow-auto h-[650px]">
                                            {supportedFraudPhases.length === 0 && ownedSuccessPhases.length === 0 && (
                                                <h1 className="text-3xl absolute top-1/2 right-1/2 translate-x-1/2 -translate-y-1/2">No funds to be claimed</h1>
                                            )}
                                            {supportedFraudPhases.length !== 0 && (
                                                <h1 className="text-3xl">Claim back funds from supported phases</h1>
                                            )}
                                            {
                                                Object.keys(supportedFraudPhases).map((key) => {
                                                    return <div key={key} className='hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150'>
                                                    <div className='font-sans text-2xl antialiased py-1'>
                                                        <p className='underline inline-block'>{supportedFraudPhases[key]['phaseName']}</p>
                                                        <p className='inline-block float-right'>Status: NEGATIVE</p>
                                                    </div><br />
                                                    <div className='font-sans text-md antialiased text-xl'>
                                                        
                                                        <button
                                                        onClick={() => {
                                                            setClickedButton(supportedFraudPhases[key]['id'])
                                                            SendFundsToDonators(bearerToken, ownedSuccessPhases[key]['id']);
                                                            setLoadingSpinner(true);
                                                        }}
                                                        className="text-white text-xl p-2 mx-1 mb-1 rounded-lg bg-green-700 transition ease-in-out delay-50 hover:scale-105 hover:bg-green-500 duration-200"
                                                        >
                                                        {loadingSpinner && Web3Error === null && Web3Success === null && clickedButton === supportedFraudPhases[key]['id'] ? (
                                                            <AiOutlineLoading3Quarters fontSize={30} className="text-white inline-block animate-spin" />
                                                        ) : (
                                                            <p>Claim</p>
                                                        )}
                                                        </button>
                                                    </div>
                                                </div>
                                                })
                                            }
                                            {ownedSuccessPhases.length !== 0 && (
                                                <h1 className="text-3xl mt-3">Claim funds from Your phases</h1>
                                            )}
                                            {
                                                Object.keys(ownedSuccessPhases).map((key) => {
                                                    return <div key={key} className='hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150'>
                                                    <div className='font-sans text-2xl antialiased py-1'>
                                                        <p className='underline inline-block'>{ownedSuccessPhases[key]['phaseName']}</p>
                                                        <p className='inline-block float-right'>Status: POSITIVE</p>
                                                    </div><br />
                                                    <div className='font-sans text-md antialiased text-xl'>
                                                        <button
                                                        onClick={() => {
                                                            setClickedButton(ownedSuccessPhases[key]['id'])
                                                            SendFundsToOwner(bearerToken, ownedSuccessPhases[key]['id'])
                                                            setLoadingSpinner(true);
                                                        }}
                                                        className="text-white text-xl p-2 mx-1 mb-1 rounded-lg bg-green-700 transition ease-in-out delay-50 hover:scale-105 hover:bg-green-500 duration-200"
                                                        >
                                                        {loadingSpinner && Web3Error === null && Web3Success === null && clickedButton === ownedSuccessPhases[key]['id']  ? (
                                                            <AiOutlineLoading3Quarters fontSize={30} className="text-white inline-block animate-spin" />
                                                        ) : (
                                                            <p>Claim</p>
                                                        )}
                                                        </button>
                                                    </div>
                                                </div>
                                                })
                                            }
                                        </div>
                                    </div>
                                ) : (
                                    <div className="h-[650px] text-white text-xl relative">
                                        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2">
                                            <AiOutlineLoading3Quarters fontSize={65} className="text-white inline-block animate-spin" />
                                        </div>
                                    </div>
                                )
                            )}

                            {commissionInfo && (
                                commissionTransactions !== null && commissionContractBalance !== null ? (
                                    <div className="relative text-white text-xl max-h-[650px] h-[650px]">
                                    <div className="overflow-auto h-[570px]">
                                    {
                                        Object.keys(commissionTransactions).map((key) => {
                                            return <div key={key} className="hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150">
                                                <p>From: <a className='underline-offset-1 inline-block underline' href={`https://goerli.etherscan.io/address/${commissionTransactions[key]['sender']}`} target='_blank' rel='noopener noreferrer'>{commissionTransactions[key]['sender']}</a></p>
                                                <p>Amount: {commissionTransactions[key]['amount']} ETH</p>
                                                <p>Comission: {commissionTransactions[key]['commissinon']} ETH</p>
                                                <p>{commissionTransactions[key]['date']}</p>
                                            </div>
                                        })
                                    }
                                    </div>
                                    <div className="absolute left-0 right-0 bottom-0 p-3 bg-neutral-800 w-full h-[80px]">
                                        <div className="grid gap-2 grid-cols-3 grid-rows-1 text-center">
                                            <p className="inline-block mr-3">Transactions: <br /> {commissionTransactinosQuantity}</p>
                                            {totalCommissionSpend < 0.001 ? (
                                                <p className="inline-block mr-3">Total commission spend:<br /> {totalCommissionSpend.toFixed(6)} ETH</p>
                                            ) : (
                                                <p className="inline-block mr-3">Total commission spend:<br /> {totalCommissionSpend.toFixed(3)} ETH</p>
                                            )}
                                            {commissionContractBalance < 0.001 ? (
                                            <button
                                            onClick={() => {WithdrawCommission(bearerToken); setLoadingSpinner(true)}}
                                            className="text-white inline-block rounded-lg bg-green-700 transition ease-in-out delay-50 hover:bg-green-500 duration-200"
                                            >
                                                {loadingSpinner === true && Web3Error === null && Web3Success === null ?(
                                                    <AiOutlineLoading3Quarters fontSize={30} className="text-white inline-block animate-spin" />
                                                ) : (
                                                    <p className="inline-block mr-3">Commission to withdraw:<br /> {commissionContractBalance.toFixed(6)} ETH</p>
                                                )}
                                            </button>
                                            ) : (
                                            <button
                                            onClick={() => {WithdrawCommission(bearerToken); setLoadingSpinner(true)}}
                                            className="text-white inline-block rounded-lg bg-green-700 transition ease-in-out delay-50 hover:bg-green-500 duration-200"
                                            >
                                                {loadingSpinner === true && Web3Error === null && Web3Success === null ?(
                                                    <AiOutlineLoading3Quarters fontSize={30} className="text-white inline-block animate-spin" />
                                                ) : (
                                                    <p className="inline-block mr-3">Commission to withdraw:<br /> {commissionContractBalance.toFixed(3)} ETH</p>
                                                )}                                            
                                                </button>
                                            )}
                                        </div>
                                    </div>
                                </div>
                                ) : (
                                    <div className="h-[650px] text-white text-xl relative">
                                        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2">
                                            <AiOutlineLoading3Quarters fontSize={65} className="text-white inline-block animate-spin" />
                                        </div>
                                    </div>
                                )
                            )}

                            {statsInfo && (
                                adTypes !== null ?(
                                    <div className="relative text-white text-xl max-h-[650px] h-[650px]">
                                        <div className="overflow-auto h-[650px]">
                                        {
                                            Object.keys(adTypes).map((key) => {
                                                return <div key={key} className="hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150">
                                                <p>Name: {adTypes[key]['name']}</p>
                                                <p>Price: {adTypes[key]['price']} ETH</p>
                                                <p>Duration: {adTypes[key]['duration']} day(s)</p>
                                                </div>
                                                
                                            }) 
                                        }
                                        <div className="hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150 flex justify-center">
                                        <button
                                        className="text-white text-2xl p-3 mx-1 mb-1 rounded-lg bg-yellow-700 transition ease-in-out delay-50 hover:bg-yellow-500 duration-200"
                                        onClick={() => setNewAdvertisePanel(!newAdvertisePanel)}
                                        >
                                            Add new type
                                        </button>
                                        </div>
                                        {newAdvertisePanel === true && (
                                            <div className="">
                                                <Input placeholder="Name" name="adNameInput" type="text" handleChange={handleChange} />
                                                <Input placeholder="Price" name="adPriceInput" type="number" handleChange={handleChange} />
                                                <Input placeholder="Duration(days)" name="adDurationInput" type="number" handleChange={handleChange} />
                                                <div className="flex justify-center">
                                                    <button
                                                        onClick={() => {
                                                            if (passwordData['adNameInput'].length !== 0 && passwordData['adPriceInput'].length !== 0 && passwordData['adDurationInput'].length !== 0) {
                                                                AddAdvertiseType(bearerToken, passwordData['adNameInput'], passwordData['adPriceInput'], passwordData['adDurationInput'])
                                                                setSubmitLoadingSpinner(true);
                                                            } else {
                                                                setAccountError('Fill in all fields!')
                                                            }
                                                        }}
                                                        className=" text-white text-2xl p-2 mx-1 mb-1 rounded-lg bg-green-700 transition ease-in-out delay-50 hover:bg-green-500 duration-200"
                                                    >
                                                    {submitLoadingSpinner === true && Web3Error === null && Web3Success === null ?(
                                                        <AiOutlineLoading3Quarters fontSize={30} className="text-white inline-block animate-spin" />
                                                    ) : (
                                                        <p>Submit</p>
                                                    )}
                                                    </button>
                                                </div>
                                            </div>
                                        )}
                                        </div>
                                    </div>
                                ) : (
                                    <div className="h-[650px] text-white text-xl relative">
                                        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2">
                                            <AiOutlineLoading3Quarters fontSize={65} className="text-white inline-block animate-spin" />
                                        </div>
                                    </div>
                                )
                            )}

                            {advertiseHistory && (
                                adTransactions !== null && advertiseContractBalance !== null ?(
                                    <div className="relative text-white text-xl max-h-[650px] h-[650px]">
                                        <div className="overflow-auto h-[570px]">
                                        {
                                            Object.keys(adTransactions).map((key) => {
                                                return <div key={key} className="hover:bg-neutral-700 p-2 rounded-xl hover:backdrop-blur-sm hover:drop-shadow-2xl duration-150">
                                                <p>From: {adTransactions[key]['sender']}</p>
                                                <p>To: {adTransactions[key]['receiver']}</p>
                                                <p>Collection: {adTransactions[key]['collectionName']}</p>
                                                <p>Amount: {adTransactions[key]['amount']} ETH</p>
                                                <p>Type: {adTransactions[key]['adTypeName']}</p>
                                                <p>Date: {adTransactions[key]['timeOfTransaction']}</p>
                                                <p>Promoted to: {adTransactions[key]['promoTo']}</p>
                                                </div>
                                            }) 
                                        }
                                        </div>
                                        <div className="absolute left-0 right-0 bottom-0 p-3 bg-neutral-800 w-full h-[80px]">
                                        <div className="grid gap-2 grid-cols-3 grid-rows-1 text-center">
                                            <p className="inline-block mr-3">Transactions: <br /> {adTransactionsQuantity}</p>
                                            {totalAdSpend < 0.001 ? (
                                                <p className="inline-block mr-3">Total amount spend:<br /> {totalAdSpend.toFixed(6)} ETH</p>
                                            ) : (
                                                <p className="inline-block mr-3">Total amount spend:<br /> {totalAdSpend.toFixed(3)} ETH</p>
                                            )}
                                            {true < 0.001 ? (
                                            <button
                                            onClick={() => {WithdrawAdvertiseFunds(bearerToken); setLoadingSpinner(true)}}
                                            className="text-white inline-block rounded-lg bg-green-700 transition ease-in-out delay-50 hover:bg-green-500 duration-200"
                                            >
                                                {loadingSpinner === true && Web3Error === null && Web3Success === null ?(
                                                    <AiOutlineLoading3Quarters fontSize={30} className="text-white inline-block animate-spin" />
                                                ) : (
                                                    <p className="inline-block mr-3">Amount to withdraw:<br /> {advertiseContractBalance.toFixed(6)} ETH</p>
                                                )}
                                            </button>
                                            ) : (
                                            <button
                                            onClick={() => {WithdrawAdvertiseFunds(bearerToken); setLoadingSpinner(true)}}
                                            className="text-white inline-block rounded-lg bg-green-700 transition ease-in-out delay-50 hover:bg-green-500 duration-200"
                                            >
                                                {loadingSpinner === true && Web3Error === null && Web3Success === null ?(
                                                    <AiOutlineLoading3Quarters fontSize={30} className="text-white inline-block animate-spin" />
                                                ) : (
                                                    <p className="inline-block mr-3">Amount to withdraw:<br /> {advertiseContractBalance.toFixed(3)} ETH</p>
                                                )}                                            
                                                </button>
                                            )}
                                        </div>
                                        </div>
                                    </div>
                                ) : (
                                    <div className="h-[650px] text-white text-xl relative">
                                        <div className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2">
                                            <AiOutlineLoading3Quarters fontSize={65} className="text-white inline-block animate-spin" />
                                        </div>
                                    </div>
                                )
                            )}
                        </div>
                    </div>
                </div>
                <div className="w-full flex md:justify-center justify-between items-center flex-col pt-4 px-4">
                    <div className="sm:w-[90%] w-full h-[0.25px] bg-gray-400 mt-5 " />
                    <div className="sm:w-[90%] w-full flex justify-between items-center mt-3">
                        <p className="text-white text-left text-xs">@Crowdfunding with trust</p>
                        <p className="text-white text-right text-xs">All rights reserved</p>
                    </div>
                </div>
                {accountError !== null && (
                    <ErrorAlert text={accountError} close={() => { setAccountError(null); setLoadingSpinner(false); setSubmitLoadingSpinner(false); }} />
                )}
                {accountSuccess !== null && (
                    <SuccessAlert text={accountSuccess} close={() => { setAccountSuccess(null); setLoadingSpinner(false); setSubmitLoadingSpinner(false);}} />
                )}
                {Web3Error !== null && (
                    <ErrorAlert text={Web3Error} close={() => { setWeb3Error(null); setLoadingSpinner(false); setSubmitLoadingSpinner(false);}} />
                )}
                {Web3Success !== null && (
                    <SuccessAlert text={Web3Success} close={() => { setWeb3Success(null); setLoadingSpinner(false); setSubmitLoadingSpinner(false);}} />
                )}
            </div>
        )}
        </div>
    );
}

export default CollectionsView;