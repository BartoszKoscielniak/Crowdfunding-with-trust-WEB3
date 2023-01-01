import { Navbar } from "../components/homepage";
import { TransactionProvider } from '../context/TransactionContext';

const Collections = () => {
    
    return (
        <TransactionProvider>
        <Navbar />
        </TransactionProvider>
    );
}

export default Collections