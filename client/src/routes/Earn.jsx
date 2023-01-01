import { Navbar } from "../components/homepage";
import { TransactionProvider } from '../context/TransactionContext';

const Earn = () => {
    
    return (
        <TransactionProvider>
        <Navbar />
        </TransactionProvider>
    );
}

export default Earn