export default function CardField({name, value}) {
  return (
    <div className="d-flex flex-wrap">
      <div className="user-label">{name}:</div>
      <div style={{flex: "1"}}>{value}</div>
    </div>
  );
}