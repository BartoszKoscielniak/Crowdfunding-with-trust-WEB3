import { Navbar, Background, CollectionsView } from "../components";
import { AccessProvider } from '../context/AccessContext';
import { CollectionProvider } from '../context/CollectionContext';
import { Parallax, ParallaxLayer } from "@react-spring/parallax";

const Collections = () => {
    
    return (
        <AccessProvider>
            <CollectionProvider>

                    {/* <Background/> */}


                    <Navbar style = {'bg-neutral-800'}/>
                    <CollectionsView/>

            </CollectionProvider>
        </AccessProvider>
    );
}

export default Collections