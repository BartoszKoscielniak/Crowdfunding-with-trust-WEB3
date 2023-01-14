import { Navbar, PollsViewHistory } from "../components";
import { AccessProvider } from '../context/AccessContext';
import { PollProvider } from "../context/PollsContext";

const PollsHistory = () => {
    
    return (
            <PollProvider>
                <Navbar style = {'bg-neutral-800'}/>
                <PollsViewHistory/>
            </PollProvider>
    );
}

export default PollsHistory