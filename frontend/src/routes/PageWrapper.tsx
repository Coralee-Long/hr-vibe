import { PageTitle } from "@/common/PageTitle.tsx";

/**
 * PageWrapper to add a PageTitle or other common layout elements
 * around protected pages.
 */
export const PageWrapper = ({
  title,
  children,
}: {
  title: string;
  children: JSX.Element;
}) => (
  <>
    <PageTitle title={title} />
    {children}
  </>
);
