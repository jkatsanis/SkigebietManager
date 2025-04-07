/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
    "./public/index.html"
  ],
  theme: {
    extend: {
      colors: {
        'ski-blue': '#2563eb',
        'snow-white': '#f8fafc',
        'mountain-gray': '#4b5563'
      },
      spacing: {
        '128': '32rem'
      }
    }
  },
  plugins: []
} 