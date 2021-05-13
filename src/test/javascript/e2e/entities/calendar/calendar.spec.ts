import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CalendarComponentsPage, CalendarDeleteDialog, CalendarUpdatePage } from './calendar.page-object';

const expect = chai.expect;

describe('Calendar e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let calendarComponentsPage: CalendarComponentsPage;
  let calendarUpdatePage: CalendarUpdatePage;
  let calendarDeleteDialog: CalendarDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Calendars', async () => {
    await navBarPage.goToEntity('calendar');
    calendarComponentsPage = new CalendarComponentsPage();
    await browser.wait(ec.visibilityOf(calendarComponentsPage.title), 5000);
    expect(await calendarComponentsPage.getTitle()).to.eq('Calendars');
    await browser.wait(ec.or(ec.visibilityOf(calendarComponentsPage.entities), ec.visibilityOf(calendarComponentsPage.noResult)), 1000);
  });

  it('should load create Calendar page', async () => {
    await calendarComponentsPage.clickOnCreateButton();
    calendarUpdatePage = new CalendarUpdatePage();
    expect(await calendarUpdatePage.getPageTitle()).to.eq('Create or edit a Calendar');
    await calendarUpdatePage.cancel();
  });

  it('should create and save Calendars', async () => {
    const nbButtonsBeforeCreate = await calendarComponentsPage.countDeleteButtons();

    await calendarComponentsPage.clickOnCreateButton();

    await promise.all([calendarUpdatePage.setNameInput('name')]);

    expect(await calendarUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');

    await calendarUpdatePage.save();
    expect(await calendarUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await calendarComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Calendar', async () => {
    const nbButtonsBeforeDelete = await calendarComponentsPage.countDeleteButtons();
    await calendarComponentsPage.clickOnLastDeleteButton();

    calendarDeleteDialog = new CalendarDeleteDialog();
    expect(await calendarDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Calendar?');
    await calendarDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(calendarComponentsPage.title), 5000);

    expect(await calendarComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
