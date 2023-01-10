import { Navbar, UsersCollections } from "../components";
import { AccessProvider } from '../context/AccessContext';
import { CollectionProvider } from '../context/CollectionContext';

const Collections = () => {
    
    return (
        <AccessProvider>
            <CollectionProvider>

                <Navbar style = {'bg-neutral-800'}/>
                <UsersCollections/>

            </CollectionProvider>
        </AccessProvider>
    );
}

export default Collections