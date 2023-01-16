import { Navbar, Welcome, Background } from "../components";
import { Parallax, ParallaxLayer } from '@react-spring/parallax';

const App = () => {
 
  return (
      <Parallax pages={1.6}>
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
  )
}

export default App
