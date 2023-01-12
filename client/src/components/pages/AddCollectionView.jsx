import { useContext, useEffect, useState } from "react";
import { CollectionContext } from '../../context/CollectionContext';
import { AccessContext } from '../../context/AccessContext';
import { AiOutlineLoading3Quarters } from "react-icons/ai";
import { ErrorAlert, SuccessAlert, Input, TextArea } from '../../components'

const CollectionsView = () => {
    const { collectionError, collectionSuccess, setCollectionError, setCollectionSuccess, AddCollection } = useContext(CollectionContext);
    const { authenticated, bearerToken } = useContext(AccessContext);
    const [collectionData, setCollectionData] = useState({
        collectionNameInput: '', collectionDescriptionInput: '',
        phase1NameInput: '', phase1DescriptionInput: '', phase1GoalInput: 0.5, phase1TillInput: '', phase1PoeInput: '',
        phase2NameInput: '', phase2DescriptionInput: '', phase2GoalInput: 0.5, phase2TillInput: '', phase2PoeInput: '',
        phase3NameInput: '', phase3DescriptionInput: '', phase3GoalInput: 0.5, phase3TillInput: '', phase3PoeInput: '',
        phase4NameInput: '', phase4DescriptionInput: '', phase4GoalInput: 0.5, phase4TillInput: '', phase4PoeInput: ''
    })
    const [radioselected, setRadioselected] = useState("CHARITY")
    const [loadingSpinner, setLoadingSpinner] = useState(false)


    const handleChange = (e, name) => {
        if ((name === 'collectionNameInput' || name === 'phase1NameInput' || name === 'phase2NameInput' || name === 'phase3NameInput' || name === 'phase4NameInput') && e.target.value.length <= 45) {
            setCollectionData((prevState) => ({ ...prevState, [name]: e.target.value }));
        } else if ((name === 'collectionDescriptionInput' || name === 'phase1DescriptionInput' || name === 'phase2DescriptionInput' || name === 'phase3DescriptionInput' || name === 'phase4DescriptionInput') && e.target.value.length <= 255) {
            setCollectionData((prevState) => ({ ...prevState, [name]: e.target.value }));
        } else if (name === 'phase1TillInput' || name === 'phase2TillInput' || name === 'phase3TillInput' || name === 'phase4TillInput' ||
                    name === 'phase1PoeInput' || name === 'phase2PoeInput' || name === 'phase3PoeInput' || name === 'phase4PoeInput') {
            setCollectionData((prevState) => ({ ...prevState, [name]: e.target.value }));
        } else if ((name === 'phase1GoalInput' || name === 'phase2GoalInput' || name === 'phase3GoalInput' || name === 'phase4GoalInput') && e.target.value > 0.5) {
            setCollectionData((prevState) => ({ ...prevState, [name]: e.target.value }));
        }
    }

    const isSelected = (phase) => {
        return phase === radioselected
    }

    const handleRadioChange = (e, name) => {
        setRadioselected(name)
    }

    const addCollection = () => {
        if (!authenticated) {
            setCollectionError("You have to be logged in!")
        } else {
            AddCollection(collectionData, radioselected, bearerToken)
        }
    }

    useEffect(() => {

    }, [])


    return (
        <div>
            <div className="bg-neutral-800 w-full absolute mf:flex-row md:px-20 md:py-28 px-4 h-[1000px]">
                <div className="grid gap-5 grid-cols-2 grid-rows-1 pl-2">
                    <div className="text-white text-lg">
                        <h1 className="text-center text-3xl font-sans">Collection</h1>
                        <div className="relative">
                            <Input placeholder="Title(10-45 characters)" name="collectionNameInput" type="text" additionalStyling={'pb-5'} value={collectionData['collectionNameInput']} handleChange={handleChange} />
                            <p className="float-right absolute right-1 top-1/2">{collectionData['collectionNameInput'] !== null && (collectionData['collectionNameInput'].length)}</p>
                        </div>
                        <div className="relative">
                            {radioselected == "CHARITY" ? (
                                <TextArea placeholder="Description(100-255 characters)" name="collectionDescriptionInput" type="text" value={collectionData['collectionDescriptionInput']} handleChange={handleChange} additionalStyling={"h-[272px]"} />
                            ) : (
                                <TextArea placeholder="Description(100-255 characters)" name="collectionDescriptionInput" type="text" value={collectionData['collectionDescriptionInput']} handleChange={handleChange} additionalStyling={"h-[330px]"} />
                            )
                            }
                            <p className="float-right absolute right-1 bottom-4">{collectionData['collectionDescriptionInput'] !== null && (collectionData['collectionDescriptionInput'].length)}</p>
                        </div>
                        <div className="rounded-xl p-2 outline-none bg-transparent text-white border my-2">
                            <div className="text-xl inline-block ">
                                <div className="inline-block pr-3"><Input name={"CHARITY"} type="radio" checked={isSelected("CHARITY")} handleChange={handleRadioChange} /></div>
                                <p className='inline-block'>Charity</p>
                            </div>
                            <div className="text-xl inline-block float-right">
                                <div className="inline-block pr-3"><Input name={"STARTUP"} type="radio" checked={isSelected("STARTUP")} handleChange={handleRadioChange} /></div>
                                <p className='inline-block'>Startup</p>
                            </div>
                        </div>
                    </div>
                    <div className="text-white text-lg overflow-auto max-h-[530px] pr-2">
                        {radioselected == "CHARITY" ? (
                            <div>
                                <h1 className="text-center text-3xl font-sans">Phase</h1>
                                <div className="relative">
                                    <Input placeholder="Title(10-45 characters)" name="phase1NameInput" type="text" additionalStyling={'pb-5'} handleChange={handleChange} />
                                    <p className="float-right absolute right-1 top-1/2">{collectionData['phase1NameInput'] !== null && (collectionData['phase1NameInput'].length)}</p>
                                </div>
                                <div className="relative">
                                    <TextArea placeholder="Description(100-255 characters)" name="phase1DescriptionInput" type="text" handleChange={handleChange} additionalStyling={"h-[210px]"} />
                                    <p className="float-right absolute right-1 bottom-4">{collectionData['phase1DescriptionInput'] !== null && (collectionData['phase1DescriptionInput'].length)}</p>
                                </div>
                                <Input placeholder="Goal" name="phase1GoalInput" type="number" value={collectionData['phase1GoalInput']} handleChange={handleChange} />
                                <Input placeholder="Till" name="phase1TillInput" type="date" handleChange={handleChange} />
                            </div>
                        ) : (
                            <div>
                                <div>
                                    <h1 className="text-center text-3xl font-sans">Phase 1</h1>
                                    <div className="relative">
                                        <Input placeholder="Title(10-45 characters)" name="phase1NameInput" type="text" additionalStyling={'pb-5'} handleChange={handleChange} />
                                        <p className="float-right absolute right-1 top-1/2">{collectionData['phase1NameInput'] !== null && (collectionData['phase1NameInput'].length)}</p>
                                    </div>
                                    <div className="relative">
                                        <TextArea placeholder="Description(100-255 characters)" name="phase1DescriptionInput" type="text" handleChange={handleChange} additionalStyling={"h-[210px]"} />
                                        <p className="float-right absolute right-1 bottom-4">{collectionData['phase1DescriptionInput'] !== null && (collectionData['phase1DescriptionInput'].length)}</p>
                                    </div>
                                    <Input placeholder="Goal" name="phase1GoalInput" type="number" value={collectionData['phase1GoalInput']} handleChange={handleChange} />
                                    <Input placeholder="Link to evidences" name="phase1PoeInput" type="text" value={collectionData['phase1PoeInput']} handleChange={handleChange} />
                                    <Input placeholder="Till" name="phase1TillInput" type="date" handleChange={handleChange} />
                                </div>
                                <div>
                                    <h1 className="text-center text-3xl font-sans">Phase 2(Optional)</h1>
                                    <div className="relative">
                                        <Input placeholder="Title(10-45 characters)" name="phase2NameInput" type="text" additionalStyling={'pb-5'} handleChange={handleChange} />
                                        <p className="float-right absolute right-1 top-1/2">{collectionData['phase2NameInput'] !== null && (collectionData['phase2NameInput'].length)}</p>
                                    </div>
                                    <div className="relative">
                                        <TextArea placeholder="Description(100-255 characters)" name="phase2DescriptionInput" type="text" handleChange={handleChange} additionalStyling={"h-[210px]"} />
                                        <p className="float-right absolute right-1 bottom-4">{collectionData['phase2DescriptionInput'] !== null && (collectionData['phase2DescriptionInput'].length)}</p>
                                    </div>
                                    <Input placeholder="Goal" name="phase2GoalInput" type="number" value={collectionData['phase2GoalInput']} handleChange={handleChange} />
                                    <Input placeholder="Link to evidences" name="phase2PoeInput" type="text" value={collectionData['phase2PoeInput']} handleChange={handleChange} />
                                    <Input placeholder="Till" name="phase2TillInput" type="date" handleChange={handleChange} />
                                </div>
                                <div>
                                    <h1 className="text-center text-3xl font-sans">Phase 3(Optional)</h1>
                                    <div className="relative">
                                        <Input placeholder="Title(10-45 characters)" name="phase3NameInput" type="text" additionalStyling={'pb-5'} handleChange={handleChange} />
                                        <p className="float-right absolute right-1 top-1/2">{collectionData['phase3NameInput'] !== null && (collectionData['phase3NameInput'].length)}</p>
                                    </div>
                                    <div className="relative">
                                        <TextArea placeholder="Description(100-255 characters)" name="phase3DescriptionInput" type="text" handleChange={handleChange} additionalStyling={"h-[210px]"} />
                                        <p className="float-right absolute right-1 bottom-4">{collectionData['phase3DescriptionInput'] !== null && (collectionData['phase3DescriptionInput'].length)}</p>
                                    </div>
                                    <Input placeholder="Goal" name="phase3GoalInput" type="number" value={collectionData['phase3GoalInput']} handleChange={handleChange} />
                                    <Input placeholder="Link to evidences" name="phase3PoeInput" type="text" value={collectionData['phase3PoeInput']} handleChange={handleChange} />
                                    <Input placeholder="Till" name="phase3TillInput" type="date" handleChange={handleChange} />
                                </div>
                                <div>
                                    <h1 className="text-center text-3xl font-sans">Phase 4(Optional)</h1>
                                    <div className="relative">
                                        <Input placeholder="Title(10-45 characters)" name="phase4NameInput" type="text" additionalStyling={'pb-5'} handleChange={handleChange} />
                                        <p className="float-right absolute right-1 top-1/2">{collectionData['phase4NameInput'] !== null && (collectionData['phase4NameInput'].length)}</p>
                                    </div>
                                    <div className="relative">
                                        <TextArea placeholder="Description(100-255 characters)" name="phase4DescriptionInput" type="text" handleChange={handleChange} additionalStyling={"h-[210px]"} />
                                        <p className="float-right absolute right-1 bottom-4">{collectionData['phase4DescriptionInput'] !== null && (collectionData['phase4DescriptionInput'].length)}</p>
                                    </div>
                                    <Input placeholder="Goal" name="phase4GoalInput" type="number" value={collectionData['phase4GoalInput']} handleChange={handleChange} />
                                    <Input placeholder="Link to evidences" name="phase4PoeInput" type="text" value={collectionData['phase4PoeInput']} handleChange={handleChange} />
                                    <Input placeholder="Till" name="phase4TillInput" type="date" handleChange={handleChange} />
                                </div>
                            </div>
                        )}
                    </div>
                </div>
                <div className="text-white my-2">
                    <button
                        onClick={() => { setLoadingSpinner(true); addCollection() }}
                        className="text-white text-xl p-2 w-full rounded-lg bg-indigo-600 transition ease-in-out delay-50 hover:scale-105 hover:bg-indigo-400 duration-200"
                    >
                        {loadingSpinner && collectionError === null && collectionSuccess === null ? (
                            <AiOutlineLoading3Quarters fontSize={30} className="text-white inline-block animate-spin" />
                        ) : (
                            <p>Add Collection!</p>
                        )}
                    </button>
                </div>
                <div className="w-full flex md:justify-center justify-between items-center flex-col pt-4 px-4">
                    <div className="sm:w-[90%] w-full h-[0.25px] bg-gray-400 mt-5 " />
                    <div className="sm:w-[90%] w-full flex justify-between items-center mt-3">
                        <p className="text-white text-left text-xs">@Crowdfunding with trust</p>
                        <p className="text-white text-right text-xs">All rights reserved</p>
                    </div>
                </div>
                {collectionError !== null && (
                    <ErrorAlert text={collectionError} close={() => { setCollectionError(null); setLoadingSpinner(false); }} />
                )}
                {collectionSuccess !== null && (
                    <SuccessAlert text={collectionSuccess} close={() => { setCollectionSuccess(null); setLoadingSpinner(false); }} />
                )}
            </div>
        </div>
    );
}

export default CollectionsView;