import { useState, useEffect } from "react"
import { Input } from '../../components'
import { BsArrowLeftShort, BsArrowRightShort } from "react-icons/bs";

const Pagination = ({items, collectionsPerPage, rows, cols}) => {
    const [currentPage, setCurrentPage]                 = useState(1)
    const [collectionsQuantity, setCollectionsQuantity] = useState(items.length)
    const [pagesQuantity, setPagesQuantity]             = useState(Math.ceil(collectionsQuantity / collectionsPerPage))
    const [searchCollection, setSearchCollection]       = useState('')
    const [itemsDisplay, setItemsDisplay]               = useState(null)

    useEffect(() => {
        setItemsDisplay(items)
    }, [])

    const lastIndexOnPage     = currentPage * collectionsPerPage    
    const firstIndexOnPage    = lastIndexOnPage - collectionsPerPage;
    let toDisplay = []
    if(itemsDisplay !== null){
        toDisplay = itemsDisplay.slice(firstIndexOnPage, lastIndexOnPage)
    }
   
    const handleChange = (e, name) => {
        setSearchCollection(e.target.value)
        if(searchCollection.length > 1){
            toDisplay = [];
            Object.keys(items).map((key) => {
                if(toDisplay.length < collectionsPerPage){
                    if((items[key]['key'].toLowerCase()).includes(e.target.value.toLowerCase()) ||
                     (items[key]['props']['children']['props']['type'].toLowerCase()).includes(e.target.value.toLowerCase())) toDisplay.push(items[key])
                }
            })
            setCurrentPage(1)
            setItemsDisplay(toDisplay)
        }else{
            setItemsDisplay(items)
            toDisplay = items
        }
    }

    const nextPage = () => {
        if(currentPage < pagesQuantity){
            setCurrentPage(currentPage + 1)
        }
    }

    const prevoiusPage = () => {
        if (currentPage > 1){
            setCurrentPage(currentPage - 1)
        }
    }

    return (
        <div>            
            <Input id="search" placeholder="Collection name\type (min 3 characters)" name="searchInput" type="text" value={searchCollection} handleChange={handleChange} />
            <div className="relative h-[900px] w-full ">
                <button
                className="text-white text-xl absolute top-1/2 -translate-y-1/2 -left-14 rounded-lg bg-indigo-600 transition ease-in-out delay-50 hover:scale-110 hover:bg-indigo-400 duration-200"
                onClick={() => prevoiusPage()}
                >
                <BsArrowLeftShort fontSize={12} className="text-white w-12 h-12" />
                </button>
                <button
                className="text-white text-xl absolute top-1/2 -translate-y-1/2 -right-14 rounded-lg bg-indigo-600 transition ease-in-out delay-50 hover:scale-110 hover:bg-indigo-400 duration-200"
                onClick={() => nextPage()}
                >
                <BsArrowRightShort fontSize={12} className="text-white w-12 h-12" />
                </button>
                <ul className={`relative grid gap-5 grid-cols-${cols} grid-rows-${rows}`}>
                    {toDisplay}
                </ul>
            </div>
        </div>
    );
}

export default Pagination;