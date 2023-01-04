const Input = ({ placeholder, name, type, value, handleChange }) => (
    <input
      placeholder = {placeholder}
      type = {type}
      step = "0.0001"
      value = {value}
      onChange = {(e) => handleChange(e, name)}
      className = "my-2 w-full rounded-xl p-2 outline-none bg-transparent text-white border text-sm white"
    />
);

export default Input;