import { Navbar, AddCollectionView } from "../components";
import { AccessProvider } from '../context/AccessContext';
import { CollectionProvider } from '../context/CollectionContext';

const Collections = () => {
    
    return (
            <CollectionProvider>
                <Navbar style = {'bg-neutral-800'}/>
                <AddCollectionView/>
            </CollectionProvider>
    );
}

export default Collections