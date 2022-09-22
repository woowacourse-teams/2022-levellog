import { Link } from 'react-router-dom';

const CustomLink = ({ path, disabled, children }: CustomLinkProps) => {
  if (disabled) {
    return <>{children}</>;
  }

  return <Link to={path}>{children}</Link>;
};

interface CustomLinkProps {
  path: string;
  disabled: boolean;
  children: JSX.Element;
}

export default CustomLink;
