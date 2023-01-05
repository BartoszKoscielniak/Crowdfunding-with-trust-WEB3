module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      keyframes: {
        flashGradient: {
          '0%': { opacity: '0.3' },
          '70%': { opacity: '0' },
          '100%': { opacity: '0.3' },
        },
        rotation: {
          '0%': { transform: 'rotate(0.0deg)' },
          '50%': { transform: 'rotate(-180deg)' },
          '100%': { transform: 'rotate(-360deg)' },
        },
        flashGradientv2: {
          '0%': { opacity: '0' },
          '70%': { opacity: '0.4' },
          '100%': { opacity: '0' },
        },
        rotationv2: {
          '0%': { transform: 'rotate(0.0deg)' },
          '50%': { transform: 'rotate(180deg)' },
          '100%': { transform: 'rotate(360deg)' },
        }
      },
      animation: {
        'flashGradient': 'flashGradient 10s linear infinite',
        'rotation': 'rotation 30s linear infinite',
        'flashGradientv2': 'flashGradientv2 6s linear infinite',
        'rotationv2': 'rotationv2 32s linear infinite',
      },
    },
  },
  plugins: [],
}