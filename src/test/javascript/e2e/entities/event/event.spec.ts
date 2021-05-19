import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EventComponentsPage, EventDeleteDialog, EventUpdatePage } from './event.page-object';

const expect = chai.expect;

describe('Event e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let eventComponentsPage: EventComponentsPage;
  let eventUpdatePage: EventUpdatePage;
  let eventDeleteDialog: EventDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.loginWithOAuth(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Events', async () => {
    await navBarPage.goToEntity('event');
    eventComponentsPage = new EventComponentsPage();
    await browser.wait(ec.visibilityOf(eventComponentsPage.title), 5000);
    expect(await eventComponentsPage.getTitle()).to.eq('Events');
    await browser.wait(ec.or(ec.visibilityOf(eventComponentsPage.entities), ec.visibilityOf(eventComponentsPage.noResult)), 1000);
  });

  it('should load create Event page', async () => {
    await eventComponentsPage.clickOnCreateButton();
    eventUpdatePage = new EventUpdatePage();
    expect(await eventUpdatePage.getPageTitle()).to.eq('Create or edit a Event');
    await eventUpdatePage.cancel();
  });

  it('should create and save Events', async () => {
    const nbButtonsBeforeCreate = await eventComponentsPage.countDeleteButtons();

    await eventComponentsPage.clickOnCreateButton();

    await promise.all([
      eventUpdatePage.setTitleInput('title'),
      eventUpdatePage.setStart_dateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      eventUpdatePage.setEnd_dateInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      eventUpdatePage.setStart_timeInput('start_time'),
      eventUpdatePage.setEnd_timeInput('end_time'),
      eventUpdatePage.setColorInput('color'),
      eventUpdatePage.setRecurring_dayInput('recurring_day'),
      eventUpdatePage.calendarSelectLastOption(),
    ]);

    expect(await eventUpdatePage.getTitleInput()).to.eq('title', 'Expected Title value to be equals to title');
    expect(await eventUpdatePage.getStart_dateInput()).to.contain(
      '2001-01-01T02:30',
      'Expected start_date value to be equals to 2000-12-31'
    );
    expect(await eventUpdatePage.getEnd_dateInput()).to.contain('2001-01-01T02:30', 'Expected end_date value to be equals to 2000-12-31');
    expect(await eventUpdatePage.getStart_timeInput()).to.eq('start_time', 'Expected Start_time value to be equals to start_time');
    expect(await eventUpdatePage.getEnd_timeInput()).to.eq('end_time', 'Expected End_time value to be equals to end_time');
    expect(await eventUpdatePage.getColorInput()).to.eq('color', 'Expected Color value to be equals to color');
    const selectedIs_all_day = eventUpdatePage.getIs_all_dayInput();
    if (await selectedIs_all_day.isSelected()) {
      await eventUpdatePage.getIs_all_dayInput().click();
      expect(await eventUpdatePage.getIs_all_dayInput().isSelected(), 'Expected is_all_day not to be selected').to.be.false;
    } else {
      await eventUpdatePage.getIs_all_dayInput().click();
      expect(await eventUpdatePage.getIs_all_dayInput().isSelected(), 'Expected is_all_day to be selected').to.be.true;
    }
    expect(await eventUpdatePage.getRecurring_dayInput()).to.eq(
      'recurring_day',
      'Expected Recurring_day value to be equals to recurring_day'
    );

    await eventUpdatePage.save();
    expect(await eventUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await eventComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Event', async () => {
    const nbButtonsBeforeDelete = await eventComponentsPage.countDeleteButtons();
    await eventComponentsPage.clickOnLastDeleteButton();

    eventDeleteDialog = new EventDeleteDialog();
    expect(await eventDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Event?');
    await eventDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(eventComponentsPage.title), 5000);

    expect(await eventComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
