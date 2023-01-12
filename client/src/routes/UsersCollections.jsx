import { Navbar, UsersCollections } from "../components";
import { AccessProvider } from '../context/AccessContext';
import { CollectionProvider } from '../context/CollectionContext';
import { PollProvider } from "../context/PollsContext";

const Collections = () => {
    
    return (
        <AccessProvider>
            <PollProvider>
            <CollectionProvider>

                <Navbar style = {'bg-neutral-800'}/>
                <UsersCollections/>

            </CollectionProvider>
            </PollProvider>
        </AccessProvider>
    );
}

export default Collections