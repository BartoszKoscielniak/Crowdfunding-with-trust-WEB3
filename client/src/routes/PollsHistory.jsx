import { Navbar, PollsViewHistory } from "../components";
import { AccessProvider } from '../context/AccessContext';
import { PollProvider } from "../context/PollsContext";

const PollsHistory = () => {
    
    return (
        <AccessProvider>
            <PollProvider>
                <Navbar style = {'bg-neutral-800'}/>
                <PollsViewHistory/>
            </PollProvider>
        </AccessProvider>
    );
}

export default PollsHistory