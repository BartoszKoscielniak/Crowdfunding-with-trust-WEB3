import { Navbar, Background, CollectionsView } from "../components";
import { AccessProvider } from '../context/AccessContext';
import { CollectionProvider } from '../context/CollectionContext';
import { PollProvider } from '../context/PollsContext';
import { Parallax, ParallaxLayer } from "@react-spring/parallax";

const Collections = () => {
    
    return (
            <CollectionProvider>
                <PollProvider>
                    
                    {/* <Background/> */}


                    <Navbar style = {'bg-neutral-800'}/>
                    <CollectionsView/>
                </PollProvider>
            </CollectionProvider>
    );
}

export default Collections