import { Navbar, Welcome, Background } from "../components";
import { Parallax, ParallaxLayer } from '@react-spring/parallax';
import { AccessProvider } from "../context/AccessContext";

const App = () => {
 
  return (
    <AccessProvider>
      <Parallax pages={1.5}>
        <ParallaxLayer
            style={{
              backgroundSize: 'cover',
              zIndex: -1,
            }}
            sticky={{ start: 0, end: 2 }}
          >
          <Background/>
        </ParallaxLayer>
        <ParallaxLayer
        style={{
          display: 'block'
        }}
        speed={1}
        >
          <Navbar/>
          <Welcome/>
        </ParallaxLayer>
      </Parallax>
  </AccessProvider>
  )
}

export default App
