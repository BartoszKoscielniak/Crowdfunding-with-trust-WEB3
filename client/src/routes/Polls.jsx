import { Navbar, PollsView } from "../components";
import { AccessProvider } from '../context/AccessContext';
import { PollProvider } from "../context/PollsContext";

const Polls = () => {
    
    return (
        <AccessProvider>
            <PollProvider>
                <Navbar style = {'bg-neutral-800'}/>
                <PollsView/>
            </PollProvider>
        </AccessProvider>
    );
}

export default Polls