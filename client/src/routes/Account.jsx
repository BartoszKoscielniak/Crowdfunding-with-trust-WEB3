import { AccountView, Navbar } from "../components";
import { AccessProvider } from '../context/AccessContext';
import { AccountProvider } from '../context/AccountContext';
import { CollectionProvider } from '../context/CollectionContext';
import { Web3Provider } from '../context/Web3Context';

const Account = () => {
    
    return (
            <CollectionProvider>
                <AccountProvider>
                    <Web3Provider>
                        <Navbar style = {'bg-neutral-800'}/>
                        <AccountView />
                    </Web3Provider>
                </AccountProvider>
            </CollectionProvider>
    );
}

export default Account