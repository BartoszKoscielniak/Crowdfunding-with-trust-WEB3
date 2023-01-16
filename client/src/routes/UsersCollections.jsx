import { Navbar, UsersCollections } from "../components";
import { Web3Provider } from '../context/Web3Context';
import { CollectionProvider } from '../context/CollectionContext';
import { PollProvider } from "../context/PollsContext";

const Collections = () => {
    
    return (
        <PollProvider>
            <CollectionProvider>
                <Web3Provider>
                    <Navbar style = {'bg-neutral-800'}/>
                    <UsersCollections/>
                </Web3Provider>
            </CollectionProvider>
        </PollProvider>
    );
}

export default Collections