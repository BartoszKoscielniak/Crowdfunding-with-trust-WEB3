import { CollectionContext } from '../../context/CollectionContext';
import { AccessContext } from '../../context/AccessContext';
import { useContext, useEffect, useState } from 'react';
import { AiOutlineCloseCircle, AiOutlineLoading3Quarters } from "react-icons/ai";
import { Input } from '..';
import { useRef } from 'react';

const Modal = ({ open, close, collectionId }) => {
    const { collections, collectionError, collectionSuccess, setCollectionError, setCollectionSuccess, DepositFunds } = useContext(CollectionContext);
    const { authenticated, bearerToken } = useContext(AccessContext);
    const [amount, setAmount] = useState(0.005)
    const [loadingSpinner, setLoadingSpinner] = useState(false)
    const [radioselected, setRadioselected] = useState(null)
    const ref = useRef();

    const handleChange = (e, name) => {
        setAmount(e.target.value)
    }

    const handleRadioChange = (e, name) => {
        setRadioselected(name)
    }

    const clickOutside = (e) => {
        if (ref.current && e.target && !ref.current.contains(e.target)) {
            close();
            setLoadingSpinner(false);
            setAmount(0.005);
        }
    };

    const sendFunds = (phaseId) => {
        if (!authenticated) {
            setCollectionError("You have to be logged in!")
        } else {
            DepositFunds(phaseId, amount, bearerToken)
        }
    }

    const isSelected = (phase) => {
        return phase === radioselected
    }

    useEffect(() => {
        document.addEventListener("click", clickOutside, true);
        if (collections !== null && collections[collectionId]['collectionPhase'][0] !== undefined) {
            setRadioselected(collections[collectionId]['collectionPhase'][0]['id'])
        }
        return () => {
            document.removeEventListener("click", clickOutside, true);
        };
    }, [close])

    return open && collections[collectionId]['collectionType'] === 'STARTUP' ? (
        <div className='w-3/4 bg-neutral-700 shadow-lg shadow-neutral-900/50 fixed top-1/2 left-1/2 p-4 rounded-3xl -translate-x-1/2 -translate-y-1/2 z-10' ref={ref}>
            <AiOutlineCloseCircle fontSize={30} className="text-white inline-block right-2 absolute top-2 cursor-pointer" onClick={() => { close(); setLoadingSpinner(false); setAmount(0.005); }} />
            <div className='pr-7'>
                <p className="font-sans text-2xl antialiased p-1 text-white inline-block">
                    {collections[collectionId]['collectionName']}
                </p>
                <p className='text-2xl text-white float-right inline-block'>
                    Supporters: {collections[collectionId]['collUserRelations'].length - 1}
                </p>
            </div>
            <div className='py-2 grid gap-5 grid-cols-2 grid-rows-1'>
                <div className='text-white text-md px-2 max-h-[620px] min-h-[300px] relative'>
                    {collections[collectionId]['baseDescription']}
                    <div className='bottom-1 absolute w-full'>
                        <Input id="amountInput" placeholder="Amount" name="amountInput" type="number" value={amount} handleChange={handleChange} />
                        <button
                            onClick={() => { 
                                sendFunds(radioselected);
                                 setLoadingSpinner(true);
                                  setCollectionSuccess(null);
                                   setCollectionError(null) 
                            }}
                            className="text-white text-xl p-2 w-full rounded-lg bg-indigo-600 transition ease-in-out delay-50 hover:scale-105 hover:bg-indigo-400 duration-200"
                        >
                            {loadingSpinner && collectionError === null && collectionSuccess === null ? (
                                <AiOutlineLoading3Quarters fontSize={30} className="text-white inline-block animate-spin" />
                            ) : (
                                <p>Support!</p>
                            )}
                        </button>
                    </div>
                </div>
                <div className='mx-2 text-white text-md px-2 max-h-[620px] overflow-auto'>
                    {
                        Object.keys(collections[collectionId]['collectionPhase']).map((key) => {
                            return <div key={key}>
                                <div className='font-sans text-2xl antialiased'>
                                    <div className='inline-block pr-3'>
                                        <Input id="phaseInput" name={collections[collectionId]['collectionPhase'][key]['id']} type="radio" checked={isSelected(collections[collectionId]['collectionPhase'][key]['id'])} handleChange={handleRadioChange} /> 
                                    </div>
                                    <p className='underline inline-block'>{collections[collectionId]['collectionPhase'][key]['phaseName']}</p>
                                    <p className='inline-block float-right'>{collections[collectionId]['collectionPhase'][key]['actualFunds'].toFixed(4)}/{collections[collectionId]['collectionPhase'][key]['goal']} ETH</p>
                                </div><br />
                                <div className='font-sans text-md antialiased'>
                                    <p>{collections[collectionId]['collectionPhase'][key]['description']}</p>
                                    <p>Till: {collections[collectionId]['collectionPhase'][key]['till'].substring(0, collections[collectionId]['collectionPhase'][0]['till'].indexOf('T'))}</p><br />
                                </div>
                            </div>
                        })
                    }
                </div>
            </div>
        </div>
    ) : open && collections[collectionId]['collectionType'] === 'CHARITY' && (
        <div className='w-3/4 bg-neutral-700 shadow-lg shadow-indigo-900/50 fixed top-1/2 left-1/2 p-4 rounded-3xl -translate-x-1/2 -translate-y-1/2 z-10' ref={ref}>
            <AiOutlineCloseCircle fontSize={30} className="text-white inline-block right-2 absolute top-2 cursor-pointer" onClick={() => { close(); setLoadingSpinner(false); setAmount(0.005); }} />
            <div className='pr-7'>
                <p className="font-sans text-2xl antialiased p-1 text-white inline-block">
                    {collections[collectionId]['collectionName']}
                </p>
                <p className='text-2xl text-white float-right inline-block'>
                    Supporters: {collections[collectionId]['collUserRelations'].length - 1}
                </p>
            </div>
            <div className='py-2 grid gap-5 grid-cols-2 grid-rows-1'>
                <div className='text-white text-md max-h-[275px] overflow-auto'>
                    <p>{collections[collectionId]['baseDescription']}</p><br />
                    <p className='font-sans text-2xl antialiased p-1'>{collections[collectionId]['collectionPhase'][0]['phaseName']}</p>
                    <p>{collections[collectionId]['collectionPhase'][0]['description']}</p><br />
                </div>
                <div className='mx-2'>
                    <div className='relative w-full border-2 h-[55px] rounded-xl'>
                        <p className='text-2xl text-white absolute top-1/2 -translate-y-1/2 right-1/2 translate-x-1/2'>{collections[collectionId]['actualFunds'].toFixed(4)} / {collections[collectionId]['goal']} ETH</p>
                    </div>
                    <Input id="amountInput" placeholder="Amount" name="amountInput" type="number" value={amount} handleChange={handleChange} />
                    <p className='text-lg text-white'>
                        Till: {collections[collectionId]['collectionPhase'][0]['till'].substring(0, collections[collectionId]['collectionPhase'][0]['till'].indexOf('T'))}
                    </p>
                    <button
                        onClick={() => { 
                            sendFunds(collections[collectionId]['collectionPhase'][0]['id']); 
                            setLoadingSpinner(true); 
                            setCollectionSuccess(null); 
                            setCollectionError(null) 
                        }}
                        className="text-white text-xl p-2 w-full rounded-lg bg-indigo-600 transition ease-in-out delay-50 hover:scale-105 hover:bg-indigo-400 duration-200"
                    >
                        {loadingSpinner && collectionError === null && collectionSuccess === null ? (
                            <AiOutlineLoading3Quarters fontSize={30} className="text-white inline-block animate-spin" />
                        ) : (
                            <p>Support!</p>
                        )}
                    </button>
                </div>
            </div>
        </div>
    );
}

export default Modal;