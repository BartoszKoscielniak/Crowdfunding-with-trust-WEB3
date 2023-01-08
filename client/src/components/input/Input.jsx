const Input = ({ placeholder, name, type, value, handleChange, checked }) => (
    <input
      placeholder = {placeholder}
      type = {type}
      step = "0.0001"
      value = {value}
      onChange = {(e) => handleChange(e, name)}
      checked = {checked}
      className = "my-2 w-full rounded-xl p-2 outline-none bg-transparent text-white border text-md"
    />
);

export default Input;