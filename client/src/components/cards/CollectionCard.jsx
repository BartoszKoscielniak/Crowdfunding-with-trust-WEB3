import { useState, useContext } from "react";
import { CollectionModal } from "..";
import { CollectionContext } from "../../context/CollectionContext";
import { PollContext } from "../../context/PollsContext";
import { AccessContext } from "../../context/AccessContext";

const CollectionCard = ({ arrayId, id, title, description, goal, type, promo, actualFunds, onClickFunction}) => {
    const [modalOpen, setModalOpen] = useState(false)
    const { setOpenedCollectionModal } = useContext(CollectionContext);
    const { GetPollById } = useContext(PollContext);
    const { bearerToken } = useContext(AccessContext);


    let style = 'my-5 w-full h-[400px] overflow-hidden bg-indigo-400 rounded-xl opacity-60 hover:opacity-100 drop-shadow-xl backdrop-blur-3xl transition ease-in-out delay-150 hover:-translate-y-1 hover:scale-105 hover:bg-indigo-600 duration-300'
    if(promo){
        style = 'my-5 w-full h-[400px] overflow-hidden bg-violet-900 border-dashed border-2 border-yellow-400 rounded-xl opacity-60 hover:opacity-100 drop-shadow-xl backdrop-blur-3xl transition ease-in-out delay-150 hover:-translate-y-1 hover:scale-105 hover:bg-indigo-600 duration-300'
    }

    return (
        <div>
            <CollectionModal open = {modalOpen} close = {() => {setModalOpen(false)}} collectionId = {arrayId} />
            {title !== '' ? (
                <div 
                className={`${style}`} 
                onClick={() => {setModalOpen(onClickFunction); GetPollById(bearerToken, id); setOpenedCollectionModal(null); setOpenedCollectionModal(arrayId); }}
                >
                    <div className="w-full p-5 antialiased">
                        <div className="overflow-hidden">
                            <div className="mb-3 text-xl text-white">
                                <h2 className="break-words">{title}</h2>
                            </div>
                            <div className="text-lg text-white">
                                <p>Collection Type: {type}</p> 
                                <p>{parseFloat(actualFunds).toFixed(2)}/{parseFloat(goal).toFixed(2)} ETH</p>
                                <p className="mt-5 break-words">{description}</p>
                            </div>
                        </div>
                    </div>
                </div>
            ) : (
                <div>
                    <div className="my-5 w-full h-[400px] bg-indigo-400 rounded-xl opacity-40 hover:opacity-100 drop-shadow-xl backdrop-blur-3xl transition ease-in-out delay-150 hover:-translate-y-1 hover:scale-105 hover:bg-indigo-600 duration-300">
                        <div className="w-full p-5 antialiased animate-pulse">
                            <div>
                                <div className="mb-10 text-xl">
                                    <div className="h-2 bg-zinc-700 rounded"></div>
                                </div>
                                <div className="grid grid-cols-3 gap-16">
                                    <div className="w-1/3 h-2 bg-zinc-700 rounded col-span-2"></div>
                                    <div className="w-1/3 h-2 bg-zinc-700 rounded col-span-2"></div>
                                    <div className="w-1/3 h-2 bg-zinc-700 rounded col-span-2"></div>
                                    <div className="w-1/3 h-2 bg-zinc-700 rounded col-span-2 "></div>
                                    <div className="w-1/3 h-2 bg-zinc-700 rounded col-span-2"></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}

export default CollectionCard;