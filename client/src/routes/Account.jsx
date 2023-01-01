import { Navbar } from "../components/homepage";
import { TransactionProvider } from '../context/TransactionContext';

const Account = () => {
    
    return (
        <TransactionProvider>
        <Navbar />
        </TransactionProvider>
    );
}

export default Account